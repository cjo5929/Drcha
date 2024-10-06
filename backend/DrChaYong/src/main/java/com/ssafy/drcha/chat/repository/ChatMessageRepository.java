package com.ssafy.drcha.chat.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.ssafy.drcha.chat.entity.ChatMessage;


@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String>, ChatMessageRepositoryCustom  {
	Page<ChatMessage> findByChatRoomIdOrderByCreatedAt(String chatRoomId, Pageable pageable);
	Page<ChatMessage> findByChatRoomIdOrderByCreatedAtDesc(String chatRoomId, Pageable pageable);
	List<ChatMessage> findByChatRoomIdOrderByCreatedAt(String chatRoomId);


}
