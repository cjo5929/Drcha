package com.ssafy.drcha.iou.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.drcha.global.error.response.ErrorResponse;
import com.ssafy.drcha.iou.service.IouService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/iou")
@RequiredArgsConstructor
public class IouController {

	private final IouService iouService;

	@Operation(summary = "차용증 생성", description = "특정 채팅방의 대화 내용을 분석하여 차용증을 생성합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "차용증 생성 성공",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = Void.class))),
		@ApiResponse(responseCode = "404", description = "채팅방을 찾을 수 없음",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "401", description = "사용자 인증 필요",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ErrorResponse.class)))
	})
	@PostMapping("/{chatRoomId}")
	public ResponseEntity<Void> createIou(@PathVariable Long chatRoomId, @AuthenticationPrincipal UserDetails userDetails) {
		iouService.createIou(chatRoomId);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}
}
