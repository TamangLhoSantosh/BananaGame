package com.tamanglhosantosh.bananagame.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfig is the main configuration class that allows custom
 * configuration.
 * The configuration class defines the layers and the flow of the filters in the
 * application.
 */
@Configuration
public class SecurityConfig {

    /**
     * Service for loading data during authorization process.
     */
    private final UserDetailsService userDetailsService;

    /**
     * Component of Spring that filters the incoming HTTP requests to
     * * authenticate the players based on the jwt tokens.
     */
    private final JwtFilter jwtFilter;

    /**
     * Constructor for SecurityConfig.
     *
     * @param userDetailsService The service used to load user-specific data (e.g., for authentication).
     * @param jwtFilter The filter responsible for validating and processing JWT tokens in requests.
     */
    public SecurityConfig(UserDetailsService userDetailsService, JwtFilter jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtFilter = jwtFilter;
    }

    /**
     * Configures HTTP security, specifying which endpoints are publicly accessible,
     * disabling CSRF (as it is not needed for stateless authentication), and
     * setting
     * up JWT-based stateless session management.
     *
     * @param http HttpSecurity object to customize HTTP security behavior
     * @return a configured SecurityFilterChain instance
     * @throws Exception if an error occurs while building the security filter chain
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // disables CSRF token
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("api/player/register", "api/player/login") // allows unauthenticated access
                        .permitAll()
                        .anyRequest().authenticated()) // allows authenticated access only
                .httpBasic(Customizer.withDefaults()) // enables basic http security
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // stateless
                                                                                                              // http
                                                                                                              // request
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // ensures jwtfilter before
                                                                                        // usernameoasswordauthenticationfilter
                .build(); // apply all the specified filters
    }

    /**
     * Creates and configures an AuthenticationProvider to retrieve player
     * information.
     *
     * @return a configured DaoAuthenticationProvider instance
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        authProvider.setUserDetailsService(userDetailsService);
        return authProvider;
    }

    /**
     * Provides the AuthenticationManager for handling authentication requests.
     * This bean is needed for authenticating requests based on the application's
     * configuration.
     *
     * @param config the AuthenticationConfiguration to retrieve the
     *               AuthenticationManager from
     * @return a configured AuthenticationManager instance
     * @throws Exception if an error occurs while creating the AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Configures BCryptPasswordEncoder for hashing passwords with a strength of 12.
     *
     * @return a configured BCryptPasswordEncoder instance
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }
}
