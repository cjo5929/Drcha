package com.ssafy.drcha.global.redis;

import org.springframework.stereotype.Service;

@Service
public class RedisSubscriber {

	public void handleMessage(String message) {
		System.out.println("Received message from Redis: " + message);
	}
}
