package com.ssafy.drcha.chat.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ssafy.drcha.global.basetime.BaseTimeEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "chat_room")
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
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
	private ChatRoom(String lastMessageId, String lastMessage, LocalDateTime lastMessageTime) {
		this.lastMessageId = lastMessageId;
		this.lastMessage = lastMessage;
		this.lastMessageTime = lastMessageTime;
	};

	public void updateLastMessage(String messageId, String message, LocalDateTime messageTime) {
		this.lastMessageId = messageId;
		this.lastMessage = message;
		this.lastMessageTime = messageTime;
	}

	public void addMember(ChatRoomMember member) {
		chatRoomMembers.add(member);
	}

	public void removeMember(ChatRoomMember member) {
		chatRoomMembers.remove(member);
	}

}
