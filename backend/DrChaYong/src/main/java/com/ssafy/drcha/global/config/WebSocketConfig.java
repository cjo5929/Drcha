package com.ssafy.drcha.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	@Value("${spring.rabbitmq.host}")
	private String rabbitHost;

	@Value("${spring.rabbitmq-stomp.port}")
	private int rabbitStompPort;

	@Value("${spring.rabbitmq.username}")
	private String username;

	@Value("${spring.rabbitmq.password}")
	private String passcode;

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.setPathMatcher(new AntPathMatcher("."));
		config.enableStompBrokerRelay("/queue", "/topic", "/exchange", "/amq/queue")
			.setRelayHost(rabbitHost)
			.setRelayPort(rabbitStompPort)
			.setSystemLogin(username)
			.setSystemPasscode(passcode)
			.setClientLogin(username)
			.setClientPasscode(passcode);

		config.setApplicationDestinationPrefixes("/pub");


	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws")
			.setAllowedOrigins("https://127.0.0.1:5500", "https://localhost:5173")
			.withSockJS();
	}
}