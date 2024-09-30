package com.ssafy.drcha.chat.dto;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import com.ssafy.drcha.chat.entity.ChatRoom;
import com.ssafy.drcha.chat.entity.ChatRoomMember;
import com.ssafy.drcha.iou.entity.Iou;
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
	private String email;
	private int unreadCount;

	public static ChatRoomListResponseDTO from(ChatRoomMember chatRoomMember, Iou iou) {
		ChatRoom chatRoom = chatRoomMember.getChatRoom();
		// 상대방 Member 찾기
		Member opponent = chatRoom.getChatRoomMembers().stream()
			.filter(member -> !member.getMember().getUsername().equals(chatRoomMember.getMember().getUsername()))
			.map(ChatRoomMember::getMember)
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("상대방 사용자를 찾을 수 없습니다."));

		return new ChatRoomListResponseDTO(
			chatRoom.getChatRoomId(),
			opponent.getUsername(),
			opponent.getAvatarUrl(),
			getContractStatus(iou),
			chatRoom.getLastMessage(),
			getIouAmount(iou),
			getDaysUntilDue(iou),
			opponent.getEmail(),
			chatRoomMember.getUnreadCount()
		);
	}

	private static String getContractStatus(Iou iou) {
		return (iou != null) ? iou.getContractStatus().name() : ContractStatus.DRAFTING.name();
	}

	private static Double getIouAmount(Iou iou) {
		return (iou != null) ? iou.getIouAmount().doubleValue() : null;
	}

	private static Long getDaysUntilDue(Iou iou) {
		if (iou != null && iou.getContractEndDate() != null) {
			return ChronoUnit.DAYS.between(LocalDateTime.now(), iou.getContractEndDate());
		}
		return null;
	}
}