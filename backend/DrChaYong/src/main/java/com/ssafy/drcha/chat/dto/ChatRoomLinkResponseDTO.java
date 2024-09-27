package com.ssafy.drcha.chat.dto;

import com.ssafy.drcha.chat.entity.ChatRoom;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomLinkResponseDTO {
	private String inviteLink;

	public static ChatRoomLinkResponseDTO from(ChatRoom chatRoom) {
		return ChatRoomLinkResponseDTO.builder()
			.inviteLink("/chat/" + chatRoom.getChatRoomId() + "/join")
			.build();
	}
}