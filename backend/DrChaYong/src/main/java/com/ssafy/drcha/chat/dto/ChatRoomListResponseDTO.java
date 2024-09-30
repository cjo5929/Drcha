package com.ssafy.drcha.chat.dto;

import com.ssafy.drcha.chat.entity.ChatRoom;
import com.ssafy.drcha.member.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class ChatRoomListResponseDTO {
	private Long chatRoomId;
	private String name;
	private String avatarUrl;
	private String contractStatus;
	private String lastMessage;
	private Double iouAmount;
	private Long daysUntilDue;
	private String email;
	private int unreadCount;

	public static ChatRoomListResponseDTO from(
		ChatRoom chatRoom,
		Member opponent,
		String contractStatus,
		Double iouAmount,
		Long daysUntilDue,
		int unreadCount) {
		return new ChatRoomListResponseDTO(
			chatRoom.getChatRoomId(),
			opponent.getUsername(),
			opponent.getAvatarUrl(),
			contractStatus,
			chatRoom.getLastMessage(),
			iouAmount,
			daysUntilDue,
			opponent.getEmail(),
			unreadCount
		);
	}
}