package com.ssafy.drcha.chat.dto;

import com.ssafy.drcha.chat.entity.ChatRoom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public final class ChatRoomCreateRequestDTO {

	private Long creditorId;

	public ChatRoom toEntity() {
		return ChatRoom.builder()
			.lastMessageId(null)
			.lastMessage(null)
			.lastMessageTime(null)
			.build();
	}

}