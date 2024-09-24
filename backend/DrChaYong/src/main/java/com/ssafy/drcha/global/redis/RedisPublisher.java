package com.ssafy.drcha.global.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import com.ssafy.drcha.chat.dto.ChatMessageDTO;
import com.ssafy.drcha.chat.dto.ChatMessageRequestDTO;

@Service
public class RedisPublisher {

	private final RedisTemplate<String, Object> redisTemplate;
	private final ChannelTopic topic;

	public RedisPublisher(RedisTemplate<String, Object> redisTemplate, ChannelTopic topic) {
		this.redisTemplate = redisTemplate;
		this.topic = topic;
	}

	public void publish(ChatMessageRequestDTO message) {
		redisTemplate.convertAndSend(topic.getTopic(), message);
	}
}
