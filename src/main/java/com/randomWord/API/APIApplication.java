package com.randomWord.API;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;



@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
@EnableJpaRepositories(basePackages = "com.randomWord.API.Repositories")
@ComponentScan(basePackages = "com.randomWord.API")
@EntityScan(basePackages = "com.randomWord.API.Entities")
public class APIApplication {

	public static void main(String[] args) {


		SpringApplication.run(APIApplication.class, args);
	
	}   

}
