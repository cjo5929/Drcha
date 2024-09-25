package com.ssafy.drcha.chat.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "chat_messages")
public class ChatMessage {

	@Id
	private Long chatRoomId;
	private Long senderId;
	private String content;
	private LocalDateTime createdAt;
	private boolean isRead;

	private ChatMessage(Long chatRoomId, Long senderId, String content, boolean isRead) {
		this.chatRoomId = chatRoomId;
		this.senderId = senderId;
		this.content = content;
		this.createdAt = LocalDateTime.now();
		this.isRead = isRead;
	}

}