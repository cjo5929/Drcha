package com.ssafy.drcha.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

	@Bean
	public WebClient webClient() {
		return WebClient.builder()
			.baseUrl("http://0.0.0.0:8000") // 로컬에서 실행 중인 Flask AI 서버의 URL
			.build();
	}
}