package com.ssafy.drcha.chat.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.ssafy.drcha.chat.enums.ChatMessageType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_messages")
public class ChatMessage {

	@Id
	private String id;
	private String chatRoomId;
	private String senderId;
	private String content;
	private LocalDateTime createdAt;
	private boolean isRead;
	private ChatMessageType messageType;

	@Builder
	public ChatMessage(String chatRoomId, String senderId, String content, boolean isRead, ChatMessageType messageType) {
		this.chatRoomId = chatRoomId;
		this.senderId = senderId;
		this.content = content;
		this.createdAt = LocalDateTime.now();
		this.isRead = isRead;
		this.messageType = messageType;
	}

}