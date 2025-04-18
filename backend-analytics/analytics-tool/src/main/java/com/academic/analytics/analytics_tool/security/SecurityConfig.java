package com.academic.analytics.analytics_tool.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth
                // ✅ Public endpoints
                .requestMatchers(
                    "/api/auth/login",
                    "/student/login",
                    "/counselor/login",
                    "/auth/google-login",
                    "student/dashboard"
                ).permitAll()

                // ✅ Role-based access
                .requestMatchers("/student/**").hasRole("STUDENT")
                .requestMatchers("/counselor/**").hasRole("COUNSELOR")

                // ✅ Anything else requires authentication
                .anyRequest().authenticated()
            )
            // ✅ Add your custom JWT filter before the default filter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
