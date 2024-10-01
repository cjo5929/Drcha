package com.ssafy.drcha.chat.repository;

import org.springframework.data.domain.Page;

import com.ssafy.drcha.chat.entity.ChatMessage;

public interface ChatMessageRepositoryCustom {
	Page<ChatMessage> findByRoomIdWithPaging(String chatRoomId, int page, int size);
}