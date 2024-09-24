package com.ssafy.drcha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DrChaYongApplication {

	public static void main(String[] args) {
		SpringApplication.run(DrChaYongApplication.class, args);
	}

}
