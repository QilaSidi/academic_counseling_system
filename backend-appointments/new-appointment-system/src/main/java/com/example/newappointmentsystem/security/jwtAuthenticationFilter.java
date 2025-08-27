package com.example.newappointmentsystem.security;



import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource; // Adjust import to your JwtUtil's package
import org.springframework.util.StringUtils; // Adjust import
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.newappointmentsystem.services.UserDetailsServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class jwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil; // Your JwtUtil from the same project

    @Autowired
    private UserDetailsServiceImpl userDetailsService; // Your UserDetailsService

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request); // Extract JWT from Authorization header
            if (jwt != null) {
                // If token exists, validate it and get user details
                try {
                    String username = jwtUtil.extractEmail(jwt); // Use extractEmail as per your JwtUtil
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    // If token is valid and user details are loaded, create an authentication object
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null, // Credentials are null for token-based auth
                                    userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Set authentication in Spring Security Context
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                } catch (Exception e) {
                    // This catches JWT parsing/validation errors (e.g., expired, invalid signature)
                   logger.error("Cannot set user authentication: {}", e);
                }
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e); // (same for the other one)
        }

        filterChain.doFilter(request, response); // Continue the filter chain
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization"); // Get Authorization header

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7); // Extract token (after "Bearer ")
        }
        return null;
    }
}