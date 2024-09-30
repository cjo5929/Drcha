package com.ssafy.drcha.chat.dto;

import com.ssafy.drcha.chat.entity.ChatRoom;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomLinkResponseDTO {
	private Long chatRoomId;
	private String invitationLink;

	public static ChatRoomLinkResponseDTO from(ChatRoom chatRoom) {
		return new ChatRoomLinkResponseDTO(
			chatRoom.getChatRoomId(),
			chatRoom.getInvitationLink()
		);
	}

}