package com.ssafy.drcha.chat.dto;

import com.ssafy.drcha.chat.entity.ChatRoom;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomLinkResponseDto {
	private Long chatRoomId;
	private String invitationLink;

	public static ChatRoomLinkResponseDto from(ChatRoom chatRoom) {
		return new ChatRoomLinkResponseDto(
			chatRoom.getChatRoomId(),
			chatRoom.getInvitationLink()
		);
	}

}