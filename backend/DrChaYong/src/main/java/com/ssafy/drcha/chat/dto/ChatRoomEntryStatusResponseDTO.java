package com.ssafy.drcha.chat.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomEntryStatusResponseDTO {
	private final String invitationLink;
	private final boolean needsRegistration;
	private final boolean needsVerification;
	private final boolean readyToEnter;

	public static ChatRoomEntryStatusResponseDTO from(ChatRoomEntryStatus status) {
		return ChatRoomEntryStatusResponseDTO.builder()
			.invitationLink(status.getInvitationLink())
			.needsRegistration(status.isNeedsRegistration())
			.needsVerification(status.isNeedsVerification())
			.readyToEnter(status.isReadyToEnter())
			.build();
	}
}