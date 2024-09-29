package com.ssafy.drcha.chat.factory;

import java.util.Base64;

import com.ssafy.drcha.chat.entity.ChatMessage;
import com.ssafy.drcha.chat.enums.ChatMessageType;
import com.ssafy.drcha.member.entity.Member;

public class ChatMessageFactory {

	public static final String SYSTEM_SENDER_ID = "SYSTEM";


	public static ChatMessage createSystemMessage(String chatRoomId, String content) {
		return ChatMessage.builder()
			.chatRoomId(chatRoomId)
			.senderId("SYSTEM")
			.content(content)
			.messageType(ChatMessageType.SYSTEM)
			.build();
	}

	public static ChatMessage createSystemImageMessage(String chatRoomId, byte[] imageData, String caption) {
		String base64Image = Base64.getEncoder().encodeToString(imageData);
		String content = caption + "\n[IMAGE]" + base64Image;
		return ChatMessage.builder()
			.chatRoomId(chatRoomId)
			.senderId(SYSTEM_SENDER_ID)
			.content(content)
			.messageType(ChatMessageType.SYSTEM)
			.build();
	}

	public static ChatMessage createEnterMessage(String chatRoomId, Member member) {
		return ChatMessage.builder()
			.chatRoomId(chatRoomId)
			.senderId(String.valueOf(member.getId()))
			.content(member.getUsername() + "님이 입장하셨습니다.")
			.messageType(ChatMessageType.ENTER)
			.build();
	}

	public static ChatMessage createLeaveMessage(String chatRoomId, Member member) {
		return ChatMessage.builder()
			.chatRoomId(chatRoomId)
			.senderId(String.valueOf(member.getId()))
			.content(member.getUsername() + "님이 퇴장하셨습니다.")
			.messageType(ChatMessageType.QUIT)
			.build();
	}

}