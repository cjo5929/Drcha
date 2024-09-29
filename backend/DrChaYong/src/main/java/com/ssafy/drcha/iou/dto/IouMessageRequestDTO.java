package com.ssafy.drcha.iou.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public final class IouMessageRequestDTO {
	private String messages;
	private Long chatRoomId;

}
