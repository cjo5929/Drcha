package com.ssafy.drcha.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChatTestController {
	@GetMapping("/chat-test")
	public String chatTest() {
		return "chat-test";  // chat-test.html 템플릿을 렌더링
	}
}