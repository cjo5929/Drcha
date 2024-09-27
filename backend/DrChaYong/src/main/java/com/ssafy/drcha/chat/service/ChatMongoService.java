package com.ssafy.drcha.chat.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.drcha.chat.entity.ChatMessage;
import com.ssafy.drcha.chat.repository.ChatMessageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMongoService {
	private final ChatMessageRepository chatMessageRepository;

	@Transactional
	public ChatMessage saveChatMessage(ChatMessage chatMessage) {
		chatMessageRepository.save(chatMessage);
		return chatMessage;
	}

}
