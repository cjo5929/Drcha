package com.ssafy.drcha.chat.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "chat_messages")
public class ChatMessage {

	@Id
	private String chatRoomId;
	private String senderId;
	private String content;
	private LocalDateTime createdAt;
	private boolean isRead;

	private ChatMessage(String chatRoomId, String senderId, String content, boolean isRead) {
		this.chatRoomId = chatRoomId;
		this.senderId = senderId;
		this.content = content;
		this.createdAt = LocalDateTime.now();
		this.isRead = isRead;
	}

}