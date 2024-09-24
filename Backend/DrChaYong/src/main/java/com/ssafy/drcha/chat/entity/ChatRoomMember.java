package com.ssafy.drcha.chat.entity;

import java.time.LocalDateTime;

import com.ssafy.drcha.chat.enums.MemberRole;
import com.ssafy.drcha.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_room_member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatRoomMember {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "chat_room_member_id")
	private Long chatRoomMemberId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chat_room_id", nullable = false)
	private ChatRoom chatRoom;

	@Enumerated(EnumType.STRING)
	@Column(name = "member_role", nullable = false)
	private MemberRole memberRole;

	@Column(name = "unread_count")
	private int unreadCount;

	@Column(name = "notification_enabled")
	private boolean notificationEnabled = true;

	@Column(name = "last_read_message_id")
	private String lastReadMessageId;

	@Column(name = "last_read_time")
	private LocalDateTime lastReadTime;

	@Builder
	private ChatRoomMember(Member member, ChatRoom chatRoom, MemberRole role) {
		this.member = member;
		this.chatRoom = chatRoom;
		this.memberRole = role;
	}

	public void incrementUnreadCount() {
		this.unreadCount++;
	}

	public void resetUnreadCount() {
		this.unreadCount = 0;
	}

	public void updateLastRead(String messageId, LocalDateTime readTime) {
		this.lastReadMessageId = messageId;
		this.lastReadTime = readTime;
		resetUnreadCount();
	}

	public void toggleNotification() {
		this.notificationEnabled = !this.notificationEnabled;
	}


}