package com.ssafy.drcha.member.controller;

import com.ssafy.drcha.member.dto.PhoneNumberRequest;
import com.ssafy.drcha.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
//            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(memberService.getVerificationStatusByEmail(""));
    }

    @PostMapping("/phone-number")
    public ResponseEntity<?> savePhoneNumber(@RequestBody @Valid PhoneNumberRequest phoneNumberRequest
//            @AuthenticationPrincipal UserDetails userDetails
    ) {
//        return ResponseEntity.ok(memberService.savePhoneNumber(userDetails.getUsername(), phoneNumberRequest));
        return ResponseEntity.ok(true);
    }
}
