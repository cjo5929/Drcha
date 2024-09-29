package com.ssafy.drcha.chat.dto;

import java.time.LocalDateTime;

import com.ssafy.drcha.chat.entity.ChatMessage;
import com.ssafy.drcha.chat.enums.ChatMessageType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class ChatMessageResponseDTO {
	private String id;
	private String chatRoomId;
	private String content;
	private String senderId;
	private ChatMessageType messageType;
	private LocalDateTime createdAt;

	public static ChatMessageResponseDTO from(ChatMessage chatMessage) {
		return new ChatMessageResponseDTO(
			chatMessage.getId(),
			chatMessage.getChatRoomId(),
			chatMessage.getContent(),
			chatMessage.getSenderId(),
			chatMessage.getMessageType(),
			chatMessage.getCreatedAt()
		);
	}

}