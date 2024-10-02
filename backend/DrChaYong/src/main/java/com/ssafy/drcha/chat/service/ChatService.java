package com.ssafy.drcha.chat.service;

import java.time.LocalDateTime;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.drcha.chat.dto.ChatMessageRequestDTO;
import com.ssafy.drcha.chat.dto.ChatMessageResponseDTO;
import com.ssafy.drcha.chat.entity.ChatMessage;
import com.ssafy.drcha.chat.entity.ChatRoom;
import com.ssafy.drcha.chat.entity.ChatRoomMember;
import com.ssafy.drcha.chat.factory.ChatMessageFactory;
import com.ssafy.drcha.chat.repository.ChatRoomMemberRepository;
import com.ssafy.drcha.chat.repository.ChatRoomRepository;
import com.ssafy.drcha.global.error.ErrorCode;
import com.ssafy.drcha.global.error.type.DataNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

	private final RabbitTemplate rabbitTemplate;
	private final ChatMongoService chatMongoService;
	private final SimpMessageSendingOperations messagingTemplate;
	private final ChatRoomRepository chatRoomRepository;
	private final ChatRoomMemberRepository chatRoomMemberRepository;

	private static final String CHAT_EXCHANGE_NAME = "chat.exchange";
	private static final String ROUTING_KEY = "chat.message.";

	@Transactional
	public void sendChatMessage(ChatMessageRequestDTO message) {
		log.info(message.toString());
		ChatMessage chatMessageEntity = message.toEntity();
		ChatMessageResponseDTO responseDTO = ChatMessageResponseDTO.from(chatMongoService.saveChatMessage(chatMessageEntity));

		updateChatRoomLastMessage(responseDTO.getChatRoomId(), responseDTO.getId(), responseDTO.getContent(), responseDTO.getCreatedAt());
		incrementUnreadCount(responseDTO.getChatRoomId(), responseDTO.getSenderId());

		publishMessage(responseDTO);
	}

	@Transactional
	public void sendIouImageMessage(Long chatRoomId, byte[] imageData) {
		ChatMessage chatMessage = ChatMessageFactory.createSystemImageMessage(
			String.valueOf(chatRoomId),
			imageData,
			"안녕하세요, 박사님입니다. 작성된 차용증을 확인해 주세요."
		);

		ChatMessageResponseDTO responseDTO = ChatMessageResponseDTO.from(chatMongoService.saveChatMessage(chatMessage));
		updateChatRoomLastMessage(responseDTO.getChatRoomId(), responseDTO.getId(), responseDTO.getContent(), responseDTO.getCreatedAt());
		publishMessage(responseDTO);
	}

	@Transactional
	public void updateChatRoomLastMessage(String chatRoomId, String messageId, String message, LocalDateTime messageTime) {
		log.info(chatRoomId + "@@@@@@");
		ChatRoom chatRoom = getChatRoomById(chatRoomId);
		chatRoom.updateLastMessage(messageId, message, messageTime);
	}

	@Transactional
	public void incrementUnreadCount(String chatRoomId, String senderId) {
		log.info("asdasd" + chatRoomId, senderId);
		ChatRoomMember recipientMember = chatRoomMemberRepository.findByChatRoom_ChatRoomIdAndMember_IdNot(
				Long.parseLong(chatRoomId), Long.parseLong(senderId))
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.MEMBER_NOT_FOUND));

		recipientMember.incrementUnreadCount();
	}

	private ChatRoom getChatRoomById(String chatRoomId) {
		return chatRoomRepository.findById(Long.parseLong(chatRoomId))
			.orElseThrow(() -> new DataNotFoundException(ErrorCode.CHAT_ROOM_NOT_FOUND));
	}


	private void publishMessage(ChatMessageResponseDTO message) {
		rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME, ROUTING_KEY + message.getChatRoomId(), message);
	}

	@RabbitListener(queues = "chat.queue")
	public void receiveMessage(ChatMessageResponseDTO message) {
		messagingTemplate.convertAndSend("/topic/chat." + message.getChatRoomId(), message);
	}

}