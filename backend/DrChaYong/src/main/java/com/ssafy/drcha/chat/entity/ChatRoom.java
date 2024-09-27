package com.ssafy.drcha.chat.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ssafy.drcha.chat.enums.MemberRole;
import com.ssafy.drcha.global.basetime.BaseTimeEntity;
import com.ssafy.drcha.member.entity.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name = "chat_room")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class ChatRoom extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chat_room_id")
	private Long chatRoomId;

	@Column(name = "last_message_id")
	private String lastMessageId;

	@Column(name = "last_message")
	private String lastMessage;

	@Column(name = "last_message_time")
	private LocalDateTime lastMessageTime;

	@OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ChatRoomMember> chatRoomMembers = new ArrayList<>();

	@Builder
	public ChatRoom(String lastMessageId, String lastMessage, LocalDateTime lastMessageTime) {
		this.lastMessageId = lastMessageId;
		this.lastMessage = lastMessage;
		this.lastMessageTime = lastMessageTime;
		this.chatRoomMembers = new ArrayList<>();
	}

	public void updateLastMessage(String messageId, String message, LocalDateTime messageTime) {
		this.lastMessageId = messageId;
		this.lastMessage = message;
		this.lastMessageTime = messageTime;
	}

	public void addMember(Member member, MemberRole role) {
		ChatRoomMember chatRoomMember = ChatRoomMember.createMember(member, this, role);
		this.chatRoomMembers.add(chatRoomMember);
	}

	public void removeMember(ChatRoomMember member) {
		chatRoomMembers.remove(member);
	}
}