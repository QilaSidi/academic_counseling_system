package com.example.newappointmentsystem.services;


import java.util.ArrayList;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service; // For simplicity, no roles for now

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // You might need a repository here to fetch user details from the appointments database
    // private final UserRepository userRepository;
    // @Autowired
    // public UserDetailsServiceImpl(UserRepository userRepository) {
    //     this.userRepository = userRepository;
    // }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // IMPORTANT: In a real microservices setup, if the 8080 service doesn't store
        // full user details locally, you might fetch them from the 8082 service
        // or a central user service using an internal API call.
        // For now, we'll create a dummy UserDetails object based on the email from the token.
        // This is primarily to satisfy Spring Security's UserDetailsService contract.

        // You will need to determine how to get the user's roles for this service.
        // For a student, it would typically be "STUDENT".
        // If your JWT contains the role, you might be able to extract it here or in the JwtAuthenticationFilter
        // For simplicity, this example just creates a user with no specific roles.
        // You can add logic to assign roles based on an internal lookup or another claim in the JWT if available.

        // Example: If you need to map roles from your JWT, you'd do it here or in JwtAuthenticationFilter.
        // For a student, you'd likely grant them the "ROLE_STUDENT" authority.

        // Simulating a user found (since the token was valid)
        // In a production app, you might fetch user data from a DB or another service
        // and map their actual roles/authorities.
        return new org.springframework.security.core.userdetails.User(
                email, // Username (email in this case)
                "", // Password (not used for JWT auth, but required by UserDetails)
                new ArrayList<>() // Authorities/Roles (e.g., List.of(new SimpleGrantedAuthority("ROLE_STUDENT")))
        );
    }
}