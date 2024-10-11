package com.ssafy.drcha.chat.dto;

import com.ssafy.drcha.chat.entity.ChatRoom;
import com.ssafy.drcha.member.entity.Member;

import com.ssafy.drcha.trust.dto.MemberTrustInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class ChatRoomListResponseDto {
	private Long chatRoomId;
	private String name;
	private String avatarUrl;
	private String contractStatus;
	private String lastMessage;
	private Double iouAmount;
	private Long daysUntilDue;
	private MemberTrustInfoResponse memberTrustInfoResponse;
	private int unreadCount;

	public static ChatRoomListResponseDto from(
		ChatRoom chatRoom,
		Member opponent,
		String contractStatus,
		Double iouAmount,
		Long daysUntilDue,
		MemberTrustInfoResponse memberTrustInfoResponse,
		int unreadCount) {
		return new ChatRoomListResponseDto(
			chatRoom.getChatRoomId(),
			opponent != null ? opponent.getUsername() : "Unknown",
			opponent != null ? opponent.getAvatarUrl() : null,
			contractStatus,
			chatRoom.getLastMessage(),
			iouAmount,
			daysUntilDue,
			memberTrustInfoResponse,
			unreadCount
		);
	}
}