package com.ssafy.drcha.chat.dto;

import com.ssafy.drcha.chat.entity.ChatMessage;
import com.ssafy.drcha.chat.enums.ChatMessageType;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class ChatMessageRequestDto {

	private String chatRoomId;
	private String content;
	private Long senderId;
	private ChatMessageType messageType;

	public ChatMessage toEntity() {
		return ChatMessage.builder()
			.chatRoomId(chatRoomId)
			.senderId(String.valueOf(senderId))
			.content(content)
			.messageType(messageType)
			.build();
	}

}