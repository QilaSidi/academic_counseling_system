package com.example.newappointmentsystem.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean; // <--- FIX THIS PACKAGE IMPORT
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // For CORS

import com.example.newappointmentsystem.services.UserDetailsServiceImpl; // For CORS

@Configuration
@EnableWebSecurity // Enables Spring Security's web security features
@EnableMethodSecurity // Enables method-level security (e.g., @PreAuthorize)
public class SecurityConfig {
    
@Autowired
    private UserDetailsServiceImpl userDetailsService; // Your implementation of UserDetailsService

    @Autowired
    private jwtAuthEntryPointJwt unauthorizedHandler; // Handles unauthorized requests

    @Bean
    public jwtAuthenticationFilter authenticationJwtTokenFilter() { // Your JWT filter
        return new jwtAuthenticationFilter();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() { // Used for authenticating users
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() { // Password encoder (even if not storing passwords, needed for AuthProvider)
        return new BCryptPasswordEncoder();
    }

    // CORS Configuration Source Bean (THIS IS CRUCIAL FOR YOUR PROBLEM)
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // Allow your frontend
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Allow all common methods
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept")); // Crucial for Authorization header
        configuration.setAllowCredentials(true); // Allow sending cookies/auth headers
        configuration.setMaxAge(3600L); // How long the pre-flight request can be cached
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Apply this CORS policy to all paths
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless APIs
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // **Enable CORS using the bean above**
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler)) // Handle unauthorized
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWTs are stateless
            .authorizeHttpRequests(authorize -> authorize
                // Public endpoints (no auth needed)
                .requestMatchers("/api/auth/**").permitAll() // If you have any login/auth endpoints here
                .requestMatchers("/appointments/counselors").permitAll() // Allow fetching counselors (adjust if you want this secured)
                // Secured endpoints (requires valid JWT)
                .requestMatchers("/appointments/**").authenticated() // All other /appointments require authentication
                .anyRequest().authenticated() // All other requests require authentication
            );

        http.authenticationProvider(authenticationProvider()); // Set your custom authentication provider
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class); // Add JWT filter before Spring's default

        return http.build();
    }
}
