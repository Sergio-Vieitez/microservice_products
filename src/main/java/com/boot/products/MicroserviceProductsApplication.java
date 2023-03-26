package com.boot.products;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestTemplate;

@ComponentScan(basePackages = {"controllers", "com.boot.products", "config", "services", "dao"})
@EntityScan(basePackages = {"models"})
@EnableJpaRepositories(basePackages = {"dao"})
@SpringBootApplication
public class MicroserviceProductsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroserviceProductsApplication.class, args);
    }

}
