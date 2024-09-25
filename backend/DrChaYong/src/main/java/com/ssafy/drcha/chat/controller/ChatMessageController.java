package com.ssafy.drcha.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import com.ssafy.drcha.chat.dto.ChatMessageRequestDTO;
import com.ssafy.drcha.chat.service.ChatService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

	private final ChatService chatService;


	@MessageMapping("/chat/message")
	public void message(ChatMessageRequestDTO message) {
		chatService.sendChatMessage(message);
	}


}
