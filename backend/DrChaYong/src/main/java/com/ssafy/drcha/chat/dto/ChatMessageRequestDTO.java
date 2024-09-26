package com.ssafy.drcha.chat.dto;

import com.ssafy.drcha.chat.entity.ChatMessage;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessageRequestDTO {

	private String chatRoomId;
	private String content;
	private String senderId;


	public ChatMessage toEntity() {
		return ChatMessage.builder()
			.chatRoomId(chatRoomId)
			.senderId(senderId)
			.content(content)
			.isRead(false)
			.build();
	}
}