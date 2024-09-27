package com.ssafy.drcha.chat.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.drcha.chat.dto.ChatRoomLinkResponseDTO;
import com.ssafy.drcha.chat.service.ChatRoomService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

	private final ChatRoomService chatRoomService;

	@Operation(summary = "채팅방 생성", description = "사용자가 새로운 채팅방을 생성하고 초대 링크를 반환합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "채팅방 생성 및 초대 링크 반환 성공",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = String.class)
			)),
		@ApiResponse(responseCode = "401", description = "사용자 인증 필요",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ErrorResponse.class)
			)),
	})
	@PostMapping("")
	public ResponseEntity<ChatRoomLinkResponseDTO> createChatRoom(@AuthenticationPrincipal UserDetails userDetails) {
		return ResponseEntity.ok(chatRoomService.createChatRoom(userDetails.getUsername()));
	}

	@Operation(summary = "채팅방 참여", description = "초대 링크를 클릭하여 채팅방에 참여합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "채팅방 참여 성공",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = String.class)
			)),
		@ApiResponse(responseCode = "409", description = "채무자가 이미 채팅방에 등록된 경우",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = String.class)
			)),
		@ApiResponse(responseCode = "401", description = "사용자 인증 필요",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = String.class)
			)),
		@ApiResponse(responseCode = "404", description = "채팅방을 찾을 수 없음",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = String.class)
			))
	})
	@GetMapping("/{chatRoomId}/join")
	public ResponseEntity<Void> joinChatRoom(@PathVariable Long chatRoomId, @AuthenticationPrincipal UserDetails userDetails) {
		chatRoomService.addDebtorToChatRoom(chatRoomId, userDetails.getUsername());

		return ResponseEntity.ok().build();
	}


}
