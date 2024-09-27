package com.ssafy.drcha.chat.dto;

import com.ssafy.drcha.chat.entity.ChatMessage;
import com.ssafy.drcha.chat.enums.ChatMessageType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageRequestDTO {

	private String chatRoomId;
	private String content;
	private String senderId;
	private ChatMessageType messageType;

	public ChatMessage toEntity() {
		return ChatMessage.builder()
			.chatRoomId(chatRoomId)
			.senderId(senderId)
			.content(content)
			.isRead(false)
			.messageType(messageType)
			.build();
	}
}