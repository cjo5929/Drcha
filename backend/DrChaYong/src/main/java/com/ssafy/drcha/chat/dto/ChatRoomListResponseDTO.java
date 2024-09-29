package com.ssafy.drcha.chat.dto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.ssafy.drcha.chat.entity.ChatRoom;
import com.ssafy.drcha.iou.enums.ContractStatus;
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
	private int unreadCount;

	public static ChatRoomListResponseDTO from(ChatRoom chatRoom, Member member, int unreadCount, ContractStatus contractStatus, Double iouAmount, LocalDateTime contractEndDate) {
		return new ChatRoomListResponseDTO(
			chatRoom.getChatRoomId(),
			member.getUsername(),
			member.getAvatarUrl(),
			contractStatus.name(),
			chatRoom.getLastMessage(),
			iouAmount,
			contractEndDate != null ? ChronoUnit.DAYS.between(LocalDateTime.now(), contractEndDate) : null,
			unreadCount
		);
	}
}