package com.ssafy.drcha.chat.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.ssafy.drcha.chat.dto.ChatMessageRequestDTO;
import com.ssafy.drcha.chat.entity.ChatMessage;
import com.ssafy.drcha.chat.repository.ChatMessageRepository;
import com.ssafy.drcha.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

	private final RabbitTemplate rabbitTemplate;
	private final ChatMessageRepository chatMessageRepository;
	private final MemberRepository memberRepository;
	private final ChatMongoService chatMongoService;
	private final SimpMessageSendingOperations messagingTemplate;

	private static final String CHAT_EXCHANGE_NAME = "chat.exchange";
	private static final String ROUTING_KEY = "chat.message.";


	public void sendChatMessage(ChatMessageRequestDTO message) {

		ChatMessage chatMessageEntity = message.toEntity();
		log.info(message.toString());

		chatMongoService.saveChatMessage(chatMessageEntity);

		// RabbitMQ를 통해 메시지 발행
		rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME, ROUTING_KEY + message.getChatRoomId(), message);

	}

	@RabbitListener(queues = "chat.queue")
	public void receiveMessage(ChatMessageRequestDTO message) {

		// WebSocket을 통해 채팅방 구독자들에게 메시지 전송
		messagingTemplate.convertAndSend("/topic/chatroom/" + message.getChatRoomId(), message);
	}
}
