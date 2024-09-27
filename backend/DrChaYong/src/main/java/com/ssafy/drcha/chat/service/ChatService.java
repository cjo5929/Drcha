package com.ssafy.drcha.chat.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.drcha.chat.dto.ChatMessageRequestDTO;
import com.ssafy.drcha.chat.entity.ChatMessage;
import com.ssafy.drcha.chat.enums.ChatMessageType;
import com.ssafy.drcha.chat.repository.ChatMessageRepository;
import com.ssafy.drcha.member.entity.Member;
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

	@Transactional
	public void sendChatMessage(ChatMessageRequestDTO message) {
		ChatMessage chatMessageEntity = message.toEntity();

		chatMongoService.saveChatMessage(chatMessageEntity);

		// RabbitMQ를 통해 메시지 발행
		rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME, ROUTING_KEY + message.getChatRoomId(), message);
	}

	@RabbitListener(queues = "chat.queue")
	public void receiveMessage(ChatMessageRequestDTO message) {
		// WebSocket을 통해 채팅방 구독자들에게 메시지 전송
		messagingTemplate.convertAndSend("/topic/chat." + message.getChatRoomId(), message);
	}


	@Transactional
	public ChatMessage createSystemMessage(String chatRoomId, String content) {
		ChatMessage systemMessage = ChatMessage.builder()
			.chatRoomId(chatRoomId)
			.senderId("SYSTEM")
			.content(content)
			.messageType(ChatMessageType.SYSTEM)
			.isRead(true)
			.build();
		return chatMongoService.saveChatMessage(systemMessage);
	}

	@Transactional
	public ChatMessage createEnterMessage(String chatRoomId, Member member) {
		ChatMessage enterMessage = ChatMessage.builder()
			.chatRoomId(chatRoomId)
			.senderId(String.valueOf(member.getId()))
			.content(member.getUsername() + "님이 입장하셨습니다.")
			.messageType(ChatMessageType.ENTER)
			.isRead(false)
			.build();
		return chatMongoService.saveChatMessage(enterMessage);
	}

	private String convertIdToString(Long id) {
		return String.valueOf(id);
	}

	private Long convertIdToLong(String id) {
		return Long.parseLong(id);
	}
}