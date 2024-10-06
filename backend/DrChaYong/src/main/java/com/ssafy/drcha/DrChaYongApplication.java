package com.ssafy.drcha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
	servers = {
		@Server(url = "/", description = "https://j11a205.p.ssafy.io/")
	}
)
@SpringBootApplication
@EnableJpaAuditing
public class DrChaYongApplication {

	public static void main(String[] args) {
		SpringApplication.run(DrChaYongApplication.class, args);
	}

}
