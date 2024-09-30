package com.ssafy.drcha.chat.repository;

import java.util.List;

import com.ssafy.drcha.chat.entity.ChatMessage;

public interface ChatMessageRepositoryCustom {
	List<ChatMessage> getChatMessagesAllByChatRoomAndTopId(String chatRoomId, Long top);
}