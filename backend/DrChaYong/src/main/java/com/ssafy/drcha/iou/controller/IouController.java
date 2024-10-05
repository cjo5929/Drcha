package com.ssafy.drcha.iou.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.drcha.chat.enums.MemberRole;
import com.ssafy.drcha.global.error.response.ErrorResponse;
import com.ssafy.drcha.iou.dto.IouCreateRequestDto;
import com.ssafy.drcha.iou.dto.IouDetailResponseDto;
import com.ssafy.drcha.iou.dto.IouPdfResponseDto;
import com.ssafy.drcha.iou.dto.IouResponseDto;
import com.ssafy.drcha.iou.dto.IouTransactionResponseDto;
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

	@Operation(summary = "차용증 생성", description = "특정 채팅방의 대화 내용을 분석하여 AI가 차용증을 생성합니다.")
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
	public ResponseEntity<IouPdfResponseDto> createIou(@PathVariable Long chatRoomId, @AuthenticationPrincipal UserDetails userDetails) {
		return ResponseEntity.ok(iouService.createAiIou(chatRoomId, userDetails.getUsername()));
	}


	@Operation(summary = "수동 차용증 생성", description = "사용자가 직접 입력한 정보로 차용증을 생성합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "201", description = "차용증 생성 성공",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = Void.class))),
		@ApiResponse(responseCode = "400", description = "잘못된 요청",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "채팅방을 찾을 수 없음",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "401", description = "사용자 인증 필요",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ErrorResponse.class)))
	})
	@PostMapping("/{chatRoomId}/manual")
	public ResponseEntity<Void> createManualIou(@PathVariable Long chatRoomId,
		@RequestBody IouCreateRequestDto requestDto,
		@AuthenticationPrincipal UserDetails userDetails) {
		iouService.createManualIou(chatRoomId, requestDto, userDetails.getUsername());
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}



	@Operation(summary = "채팅방 차용증 조회", description = "채팅방 ID에 맞는 차용증 리스트를 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "차용증 조회 성공",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = IouResponseDto.class))),
		@ApiResponse(responseCode = "404", description = "채팅방을 찾을 수 없음",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "401", description = "사용자 인증 필요",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ErrorResponse.class)))
	})
	@GetMapping("/{chatRoomId}")
	public ResponseEntity<List<IouResponseDto>> getIousByChatRoomId(@PathVariable Long chatRoomId, @AuthenticationPrincipal UserDetails userDetails) {
		return ResponseEntity.ok(iouService.getIousByChatRoomId(chatRoomId, userDetails.getUsername()));
	}


	@Operation(summary = "돈을 빌려준 기록 조회", description = "사용자가 돈을 빌려준 기록 (CREDITOR) 목록을 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "차용증 조회 성공",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = IouTransactionResponseDto.class))),
		@ApiResponse(responseCode = "401", description = "사용자 인증 필요",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ErrorResponse.class)))
	})
	@GetMapping("/lending-records")
	public ResponseEntity<List<IouTransactionResponseDto>> getLendingRecords(
		@AuthenticationPrincipal UserDetails userDetails) {
		return ResponseEntity.ok(iouService.getIousByRole(userDetails.getUsername(), MemberRole.CREDITOR));
	}

	@Operation(summary = "돈을 빌린 기록 조회", description = "사용자가 돈을 빌린 기록 (DEBTOR) 목록을 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "차용증 조회 성공",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = IouTransactionResponseDto.class))),
		@ApiResponse(responseCode = "401", description = "사용자 인증 필요",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ErrorResponse.class)))
	})
	@GetMapping("/borrowing-records")
	public ResponseEntity<List<IouTransactionResponseDto>> getBorrowingRecords(
		@AuthenticationPrincipal UserDetails userDetails) {
		return ResponseEntity.ok(iouService.getIousByRole(userDetails.getUsername(), MemberRole.DEBTOR));
	}

	@Operation(summary = "돈을 빌려준 차용증 상세 조회", description = "사용자가 돈을 빌려준 차용증의 상세 정보를 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "차용증 상세 조회 성공",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = IouDetailResponseDto.class))),
		@ApiResponse(responseCode = "404", description = "차용증을 찾을 수 없음",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "401", description = "사용자 인증 필요",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ErrorResponse.class)))
	})
	@GetMapping("/lending-detail/{iouId}")
	public ResponseEntity<IouDetailResponseDto> getLendingIouDetail(
		@PathVariable Long iouId, @AuthenticationPrincipal UserDetails userDetails) {
		return ResponseEntity.ok(iouService.getIouDetail(iouId, MemberRole.CREDITOR));
	}

	@Operation(summary = "돈을 빌린 차용증 상세 조회", description = "사용자가 돈을 빌린 차용증의 상세 정보를 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "차용증 상세 조회 성공",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = IouDetailResponseDto.class))),
		@ApiResponse(responseCode = "404", description = "차용증을 찾을 수 없음",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "401", description = "사용자 인증 필요",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ErrorResponse.class)))
	})
	@GetMapping("/borrowing-detail/{iouId}")
	public ResponseEntity<IouDetailResponseDto> getBorrowingIouDetail(
		@PathVariable Long iouId, @AuthenticationPrincipal UserDetails userDetails) {
		return ResponseEntity.ok(iouService.getIouDetail(iouId, MemberRole.DEBTOR));
	}


	@Operation(summary = "차용증 PDF 데이터 조회", description = "특정 차용증의 PDF 생성을 위한 데이터를 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "차용증 PDF 데이터 조회 성공",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = IouPdfResponseDto.class))),
		@ApiResponse(responseCode = "404", description = "차용증을 찾을 수 없음",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "401", description = "사용자 인증 필요",
			content = @Content(mediaType = "application/json",
				schema = @Schema(implementation = ErrorResponse.class)))
	})
	@GetMapping("/pdf/{iouId}")
	public ResponseEntity<IouPdfResponseDto> getIouPdfData(
		@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long iouId
	){
		return ResponseEntity.ok(iouService.getIouPdfData(iouId));
	}
}
