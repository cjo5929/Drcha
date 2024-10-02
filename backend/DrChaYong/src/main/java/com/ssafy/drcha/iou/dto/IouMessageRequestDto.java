package com.ssafy.drcha.iou.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class IouMessageRequestDto {
	private String messages;
	private Long chatRoomId;

}
