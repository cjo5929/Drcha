package com.ssafy.drcha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.scheduling.annotation.EnableScheduling;

@OpenAPIDefinition(
	servers = {
		@Server(url = "/", description = "https://j11a205.p.ssafy.io/")
	}
)
@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class DrChaYongApplication {

	public static void main(String[] args) {
		SpringApplication.run(DrChaYongApplication.class, args);
	}

}
