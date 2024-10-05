package com.ssafy.drcha.chat.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import com.ssafy.drcha.chat.dto.ChatMessageRequestDto;
import com.ssafy.drcha.chat.service.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatMessageController {

	private final ChatService chatService;

	@MessageMapping("chat.message")
	public void handleChatMessage(@Payload ChatMessageRequestDto message) {
		chatService.sendChatMessage(message);
	}


}
