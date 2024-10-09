package com.ssafy.drcha.chat.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.ssafy.drcha.chat.enums.ChatMessageType;
import com.ssafy.drcha.iou.dto.IouPdfResponseDto;

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
	@Field("createdAt")
	private LocalDateTime createdAt;
	private ChatMessageType messageType;
	private IouPdfResponseDto iouInfo;


	@Builder
	public ChatMessage(String chatRoomId, String senderId, String content, ChatMessageType messageType,  IouPdfResponseDto iouInfo) {
		this.chatRoomId = chatRoomId;
		this.senderId = senderId;
		this.content = content;
		this.createdAt = LocalDateTime.now();
		this.messageType = messageType;
		this.iouInfo = iouInfo;
	}

}