package com.ssafy.drcha.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChatMessageParam {
	private int page;
	private int size;
}