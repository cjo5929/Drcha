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
	private String opponentName;
	private String lastMessageId;
	private String lastMessage;
	private LocalDateTime lastMessageTime;
	private int unreadCount;
	private MemberRole memberRole;

	public static ChatRoomEntryResponseDto from(ChatRoom chatRoom, ChatRoomMember chatRoomMember, Member opponent) {
		return new ChatRoomEntryResponseDto(
			chatRoom.getChatRoomId(),
			opponent.getUsername(),
			chatRoom.getLastMessageId(),
			chatRoom.getLastMessage(),
			chatRoom.getLastMessageTime(),
			chatRoomMember.getUnreadCount(),
			chatRoomMember.getMemberRole()
		);
	}
}
