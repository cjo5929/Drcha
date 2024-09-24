package com.ssafy.drcha.chat.service;

import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import com.ssafy.drcha.chat.dto.ChatMessageRequestDTO;
import com.ssafy.drcha.chat.repository.ChatMessageRepository;
import com.ssafy.drcha.global.error.ErrorCode;
import com.ssafy.drcha.global.error.type.UserNotFoundException;
import com.ssafy.drcha.global.redis.RedisPublisher;
import com.ssafy.drcha.member.entity.Member;
import com.ssafy.drcha.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatService {

	// private final RedisPublisher redisPublisher;
	// private final SimpMessageSendingOperations messagingTemplate;
	// private final ChatMessageRepository chatMessageRepository;
	// private final MemberRepository memberRepository;


	// public void sendChatMessage(ChatMessageRequestDTO message, String email) {
	//
	// 	Member member = memberRepository.findByEmail(email)
	// 		.orElseThrow(() -> new UserNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
	// 	chatMessageRepository.save(message.toEntity(member.getId()));
	//
	// 	// Redis Pub/Sub을 통해 메시지 발행
	// 	redisPublisher.publish(message);
	//
	// 	// WebSocket을 통해 채팅방 구독자들에게 메시지 전송
	// 	messagingTemplate.convertAndSend("/topic/chatroom/" + message.getChatRoomId(), message);
	//
	//
	//
	//
	// }
}
