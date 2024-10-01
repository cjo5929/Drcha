package com.ssafy.drcha.account.controller;

import com.ssafy.drcha.account.dto.AccountResponse;
import com.ssafy.drcha.account.dto.SendVerificationCodeResponse;
import com.ssafy.drcha.account.dto.VerifyCodeResponse;
import com.ssafy.drcha.account.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/account")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @Operation(summary = "회원 계좌 목록", description = "계좌 목록을 조회한다.")
    @ApiResponse(responseCode = "200", description = "계좌 목록 조회 성공",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = AccountResponse.class))))
    @GetMapping
    public ResponseEntity<?> getMemberInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(accountService.getAccountList(userDetails.getUsername()));
    }

    @Operation(summary = "1원 송금 요청", description = "싸피 계좌 1원 송금을 요청한다.")
    @ApiResponse(responseCode = "200", description = "1원 송금 요청 성공",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation = SendVerificationCodeResponse.class)))
    @GetMapping("/verification-code")
    public ResponseEntity<?> sendVerificationCode(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(accountService.sendVerificationCode(userDetails.getUsername()));
    }

    @Operation(summary = "1원 송금 검증", description = "4자리 인증코드로 본인 계좌인지 확인한다.")
    @ApiResponse(responseCode = "200", description = "1원 송금 검증 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = VerifyCodeResponse.class)))
    @PostMapping("/verification-code")
    public ResponseEntity<?> verifyCode(@AuthenticationPrincipal UserDetails userDetails, @RequestParam("code") String code) {
        return ResponseEntity.ok(accountService.verifyCode(userDetails.getUsername(), code));
    }

    @Operation(summary = "싸피 계좌 상세 조회", description = "회원가입 시 생성된 싸피 계좌 정보를 조회한다.")
    @ApiResponse(responseCode = "200", description = "싸피 계좌 상세 조회 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AccountResponse.class)))
    @GetMapping("/detail")
    public ResponseEntity<?> getDetail(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(accountService.getDetail(userDetails.getUsername()));
    }
}
