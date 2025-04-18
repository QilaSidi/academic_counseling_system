package com.example.demo.config;

import javax.sql.DataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import jakarta.persistence.EntityManagerFactory; // Now it's actually used!
import org.springframework.beans.factory.annotation.Qualifier;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
public class SophieDatabaseConfig {

    @Bean(name = "sophieDataSource")
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/sophie_chatbot");
        dataSource.setUsername("root");
        dataSource.setPassword("Aqil2612#");
        return dataSource;
    }

    @Bean(name = "sophieEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setPackagesToScan("com.example.demo.model.sophie");

        // Set JPA vendor adapter
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        factoryBean.setJpaVendorAdapter(vendorAdapter);

        // Set JPA properties
        Properties jpaProperties = new Properties();
        jpaProperties.put("hibernate.hbm2ddl.auto", "update");
        jpaProperties.put("hibernate.show_sql", "true");
        factoryBean.setJpaProperties(jpaProperties);

        return factoryBean;
    }

    @Bean(name = "sophieTransactionManager")
    public PlatformTransactionManager transactionManagerSophie(
            @Qualifier("sophieEntityManagerFactory") EntityManagerFactory entityManagerFactory) { 
        
        return new JpaTransactionManager(entityManagerFactory);
    }
}
