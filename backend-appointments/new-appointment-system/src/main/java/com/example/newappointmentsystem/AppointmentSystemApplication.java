package com.example.newappointmentsystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.example.newappointmentsystem", "com.example.newappointmentsystem.controllers"})
@EntityScan(basePackages = "com.example.newappointmentsystem.models") // Scans for entities
@EnableJpaRepositories(basePackages = "com.example.newappointmentsystem.repositories") // Scans for repositories
public class AppointmentSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppointmentSystemApplication.class, args);
    }
}