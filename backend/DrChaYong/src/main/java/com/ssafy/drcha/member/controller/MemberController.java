package com.ssafy.drcha.member.controller;

import com.ssafy.drcha.member.dto.MemberInfoResponse;
import com.ssafy.drcha.member.dto.PhoneNumberRequest;
import com.ssafy.drcha.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @Operation(summary = "본인 인증 여부 확인", description = "사용자가 본인 인증 과정을 진행했는지 확인한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "본인 인증 상태 확인 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = boolean.class),
                            examples = @ExampleObject(value = "true"))),
            @ApiResponse(responseCode = "401", description = "사용자 인증 필요",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"error\": \"Unauthorized\", \"message\": \"인증이 필요합니다.\"}"))),
            @ApiResponse(responseCode = "404", description = "사용자 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"error\": \"Not Found\", \"message\": \"사용자를 찾을 수 없습니다.\"}")))
    })
    @GetMapping("/auth/status")
    public ResponseEntity<?> getVerificationStatus(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(memberService.getVerificationStatusByEmail(userDetails.getUsername()));
    }

    @Operation(summary = "휴대폰 번호 등록", description = "휴대폰 번호를 등록한다.")
    @ApiResponse(responseCode = "200", description = "휴대폰 번호 등록 성공")
    @PostMapping("/phone-number")
    public ResponseEntity<Void> savePhoneNumber(@RequestBody @Valid PhoneNumberRequest phoneNumberRequest,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        memberService.savePhoneNumber(userDetails.getUsername(), phoneNumberRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "로그아웃", description = "사용자를 로그아웃하고 인증 토큰을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "404", description = "리프레쉬 토큰을 찾을 수 없음",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"error\": \"Not Found\", \"message\": \"리프레시 토큰이 없습니다.\"}")))
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        return new ResponseEntity<>(memberService.logout(request, response), HttpStatus.OK);
    }

    @Operation(summary = "회원 정보", description = "회원 정보를 조회한다.")
    @ApiResponse(responseCode = "200", description = "회원 정보 조회 성공",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MemberInfoResponse.class)))
    @GetMapping("/info")
    public ResponseEntity<?> getMemberInfo(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(memberService.getMemberInfo(userDetails.getUsername()));
    }
}
