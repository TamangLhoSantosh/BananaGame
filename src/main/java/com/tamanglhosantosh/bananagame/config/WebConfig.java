package com.tamanglhosantosh.bananagame.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.Arrays;

/**
 *  WebConfig is the configuration class that configures CORS for the application.
 */
@Configuration
@EnableWebMvc
public class WebConfig  {


    /**
     * Bean for configuring CORS (Cross-Origin Resource Sharing) filter.
     *
     * @return FilterRegistrationBean with configured CorsFilter
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        // Sets up the CORS configuration source
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedHeader(HttpHeaders.AUTHORIZATION);
        config.addAllowedHeader(HttpHeaders.CONTENT_TYPE);
        config.addAllowedHeader(HttpHeaders.ACCEPT);
        config.addAllowedHeader(HttpHeaders.ORIGIN);
        config.addAllowedHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN);
        config.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.DELETE.name()
        ));
        // Sets the duration for which the CORS configuration can be cached by the client
        config.setMaxAge(3600L);

        // Applies the configuration to all routes
        source.registerCorsConfiguration("/**", config);

        // Registers the CORS filter
        return new FilterRegistrationBean<>(new CorsFilter(source));
    }
}
