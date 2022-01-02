package com.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages="com.test")
@EntityScan(basePackages = {"com.test.model"})
public class AccountServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(AccountServiceApplication.class, args);
	}
}
