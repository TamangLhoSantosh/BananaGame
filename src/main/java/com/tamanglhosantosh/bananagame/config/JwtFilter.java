package com.tamanglhosantosh.bananagame.config;

import com.tamanglhosantosh.bananagame.service.JWTService;
import com.tamanglhosantosh.bananagame.service.PlayerDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtFilter is a component of Spring that filters the incoming HTTP requests to
 * authenticate the players based on the jwt tokens.
 *
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    /**
     * Service for generating and validating jwt tokens.
     */
    @Autowired
    private JWTService jwtService;

    /**
     * The Spring application context, used to retrieve beans like PlayerDetailsService.
     */
    @Autowired
    ApplicationContext context;


    /**
     * Filters each request, checking for the presence of a jwt token in the
     * Authorization header.
     *
     * @param request     incoming HTTP request
     * @param response    HTTP response
     * @param filterChain filter chain to continue processing the request
     * @throws ServletException if a servlet error occurs during filtering
     * @throws IOException      if an I/O error occurs during filtering
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Extract JWT token from Authorization header
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // Check if the token is present and starts with "Bearer "
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Extract token from authorization header
            username = jwtService.extractUserName(token); // Extract username from token
        }

        // Authenticate player if token is valid and player is not already authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load player details from PlayerDetailsService
            UserDetails playerDetails = context.getBean(PlayerDetailsService.class).loadUserByUsername(username);

            // Validate the JWT token
            if (jwtService.validateToken(token, playerDetails)) {
                // Authenticate player in SecurityContext
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(playerDetails,
                        null, playerDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                // Send unauthorized error response if token is invalid or expired
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or expired token");
                return; // Prevent further processing
            }
        }

        // Continue filter chain for the request
        filterChain.doFilter(request, response);
    }
}
