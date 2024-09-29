package com.ssafy.drcha.chat.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.ssafy.drcha.chat.entity.ChatRoom;
import com.ssafy.drcha.chat.entity.ChatRoomMember;
import com.ssafy.drcha.iou.entity.Iou;
import com.ssafy.drcha.iou.enums.ContractStatus;
import com.ssafy.drcha.member.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class ChatRoomDetailResponseDTO {
	private Long chatRoomId;
	private String otherMemberName;
	private String otherMemberProfileImage;
	private List<ChatMessageResponseDTO> recentMessages;
	private Double iouAmount;
	private ContractStatus contractStatus;
	private LocalDateTime contractEndDate;

	public static ChatRoomDetailResponseDTO from(ChatRoom chatRoom, Member currentMember,
		List<ChatMessageResponseDTO> recentMessages, Iou iou) {
		Member otherMember = chatRoom.getChatRoomMembers().stream()
			.filter(crm -> !crm.getMember().getId().equals(currentMember.getId()))
			.findFirst()
			.map(ChatRoomMember::getMember)
			.orElseThrow(() -> new IllegalStateException("Other member not found in chat room"));

		return new ChatRoomDetailResponseDTO(
			chatRoom.getChatRoomId(),
			otherMember.getUsername(),
			otherMember.getAvatarUrl(),
			recentMessages,
			iou != null ? iou.getIouAmount().doubleValue() : null,
			iou != null ? iou.getContractStatus() : ContractStatus.DRAFTING,
			iou != null ? iou.getContractEndDate() : null
		);
	}
}