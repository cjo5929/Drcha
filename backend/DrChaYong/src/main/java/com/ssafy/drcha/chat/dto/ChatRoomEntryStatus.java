package com.ssafy.drcha.chat.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomEntryStatus {
	private final Long chatRoomId;
	private final boolean needsRegistration;
	private final boolean needsVerification;

}