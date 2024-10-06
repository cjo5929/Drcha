package com.ssafy.drcha.chat.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import com.ssafy.drcha.chat.dto.ChatMessageRequestDto;
import com.ssafy.drcha.chat.service.ChatRoomService;
import com.ssafy.drcha.chat.service.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatMessageController {

	private final ChatService chatService;
	private final ChatRoomService chatRoomService;
	private final SimpMessagingTemplate messagingTemplate; // SimpMessagingTemplate 추가하여 특정 사용자에게 메시지 전송


	@MessageMapping("chat.message")
	public void handleChatMessage(@Payload ChatMessageRequestDto message) {
		chatService.sendChatMessage(message);
	}

	@MessageMapping("/chat/loadMore/{chatRoomId}")
	public void loadMoreMessages(
		@DestinationVariable Long chatRoomId,
		@AuthenticationPrincipal UserDetails userDetails,
		SimpMessageHeaderAccessor headerAccessor) {

		chatService.loadAllMessagesAndSend(String.valueOf(chatRoomId));
	}


}
