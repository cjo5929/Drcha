package com.ssafy.drcha.chat.dto;

import java.time.LocalDateTime;

import com.ssafy.drcha.chat.entity.ChatRoom;
import com.ssafy.drcha.chat.entity.ChatRoomMember;
import com.ssafy.drcha.chat.enums.MemberRole;
import com.ssafy.drcha.member.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatRoomEntryResponseDto {
	private Long chatRoomId;
	private Long creditorId;
	private Long debtorId;
	private String opponentName;
	private String lastMessageId;
	private String lastMessage;
	private LocalDateTime lastMessageTime;
	private int unreadCount;
	private MemberRole memberRole;

	// opponent가 null일 경우 처리
	public static ChatRoomEntryResponseDto from(ChatRoom chatRoom, ChatRoomMember chatRoomMember, Member opponent,
		Member creditor, Member debtor) {

		return new ChatRoomEntryResponseDto(
			chatRoom.getChatRoomId(),
			creditor != null ? creditor.getId() : null,
			debtor != null ? debtor.getId() : null,
			opponent != null ? opponent.getUsername() : null,
			chatRoom.getLastMessageId(),
			chatRoom.getLastMessage(),
			chatRoom.getLastMessageTime(),
			chatRoomMember.getUnreadCount(),
			chatRoomMember.getMemberRole()
		);
	}
}