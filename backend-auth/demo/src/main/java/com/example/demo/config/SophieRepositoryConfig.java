package com.example.demo.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@Configuration
@EnableJpaRepositories(
basePackages = "com.example.demo.repository.sophie",
entityManagerFactoryRef = "sophieEntityManagerFactory",
transactionManagerRef = "sophieTransactionManager")

@ComponentScan(basePackages = "com.example.demo.repository.sophie")
public class SophieRepositoryConfig {
    
}
