package com.ssafy.drcha.chat.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.drcha.chat.dto.ChatMessageParam;
import com.ssafy.drcha.chat.dto.ChatMessageResponseDto;
import com.ssafy.drcha.chat.dto.ChatRoomLinkResponseDto;
import com.ssafy.drcha.chat.dto.ChatRoomListResponseDto;
import com.ssafy.drcha.chat.enums.MemberRole;
import com.ssafy.drcha.chat.service.ChatRoomService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("ws/api/v1/chat")
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
	@PostMapping
	public ResponseEntity<ChatRoomLinkResponseDto> createChatRoom(@AuthenticationPrincipal UserDetails userDetails) {
		return ResponseEntity.ok(chatRoomService.createChatRoom(userDetails.getUsername()));
	}

	@Operation(summary = "빌려준 채팅방 목록 조회", description = "사용자가 빌려준 모든 채팅방의 목록을 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "채팅방 목록 조회 성공",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ChatRoomListResponseDto.class))),
		@ApiResponse(responseCode = "401", description = "사용자 인증 필요",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ErrorResponse.class)))
	})
	@GetMapping("/lent")
	public ResponseEntity<List<ChatRoomListResponseDto>> getLentChatRoomList(@AuthenticationPrincipal UserDetails userDetails) {
		return ResponseEntity.ok(chatRoomService.getChatRoomListByRole(userDetails.getUsername(), MemberRole.CREDITOR));
	}

	@Operation(summary = "빌린 채팅방 목록 조회", description = "사용자가 빌린 모든 채팅방의 목록을 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "채팅방 목록 조회 성공",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ChatRoomListResponseDto.class))),
		@ApiResponse(responseCode = "401", description = "사용자 인증 필요",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ErrorResponse.class)))
	})
	@GetMapping("/borrowed")
	public ResponseEntity<List<ChatRoomListResponseDto>> getBorrowedChatRoomList(@AuthenticationPrincipal UserDetails userDetails) {
		return ResponseEntity.ok(chatRoomService.getChatRoomListByRole(userDetails.getUsername(), MemberRole.DEBTOR));
	}

	@Operation(summary = "채팅방 입장", description = "사용자가 특정 채팅방에 입장하여 메시지를 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "채팅방 입장 및 메시지 조회 성공",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ChatMessageResponseDto.class))),
		@ApiResponse(responseCode = "401", description = "사용자 인증 필요",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "채팅방 또는 사용자를 찾을 수 없음",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ErrorResponse.class)))
	})
	@GetMapping("/{chatRoomId}/enter")
	public ResponseEntity<List<ChatMessageResponseDto>> enterChatRoom(
		@Parameter(description = "채팅방 아이디", required = true)
		@PathVariable Long chatRoomId,
		@Parameter(description = "인증된 사용자 정보", hidden = true)
		@AuthenticationPrincipal UserDetails userDetails) {

		return ResponseEntity.ok(chatRoomService.enterChatRoom(chatRoomId, userDetails.getUsername()));
	}

	@Operation(summary = "초대 링크로 채팅방 입장", description = "사용자가 초대 링크를 통해 채팅방에 입장하여 메시지를 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "채팅방 입장 및 메시지 조회 성공",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ChatMessageResponseDto.class))),
		@ApiResponse(responseCode = "302", description = "회원가입 또는 본인인증 필요로 인한 리다이렉트",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "401", description = "사용자 인증 필요",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "채팅방 또는 사용자를 찾을 수 없음",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ErrorResponse.class)))
	})
	@GetMapping("/{invitationLink}/link/enter")
	public ResponseEntity<List<ChatMessageResponseDto>> enterChatRoomViaInvitationLink(
		@Parameter(description = "채팅방 링크", required = true)
		@PathVariable String invitationLink,
		@Parameter(description = "인증된 사용자 정보", hidden = true)
		@AuthenticationPrincipal UserDetails userDetails) {
		return ResponseEntity.ok(chatRoomService.enterChatRoomViaLink(invitationLink, userDetails));
	}

	@Operation(summary = "추가 채팅 메시지 로드", description = "채팅방에서 스크롤을 통해 이전 메시지를 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "추가 메시지 조회 성공",
			content = @Content(mediaType = "application/json",
				array = @ArraySchema(schema = @Schema(implementation = ChatMessageResponseDto.class)))),
		@ApiResponse(responseCode = "401", description = "사용자 인증 필요",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ErrorResponse.class)))
	})
	@GetMapping("/{chatRoomId}/messages")
	public ResponseEntity<List<ChatMessageResponseDto>> loadMoreMessages(
		@Parameter(description = "채팅방 ID", required = true)
		@PathVariable Long chatRoomId,
		@Parameter(description = "인증된 사용자 정보", hidden = true)
		@AuthenticationPrincipal UserDetails userDetails,
		@Parameter(description = "메시지 조회 파라미터", required = true)
		@ModelAttribute ChatMessageParam param) {

		return ResponseEntity.ok(chatRoomService.loadMoreMessages(chatRoomId, userDetails.getUsername(), param));
	}
}


