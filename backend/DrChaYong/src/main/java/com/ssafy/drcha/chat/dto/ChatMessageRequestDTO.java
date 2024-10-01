package com.ssafy.drcha.chat.dto;

import com.ssafy.drcha.chat.entity.ChatMessage;
import com.ssafy.drcha.chat.enums.ChatMessageType;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class ChatMessageRequestDTO {

	private String chatRoomId;
	private String content;
	private String senderId;
	private ChatMessageType messageType;
	private String avatarUrl;

	public ChatMessage toEntity() {
		return ChatMessage.builder()
			.chatRoomId(chatRoomId)
			.senderId(senderId)
			.content(content)
			.messageType(messageType)
			.avatarUrl(avatarUrl)
			.build();
	}

}