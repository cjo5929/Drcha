package com.ssafy.drcha.member.controller;

import com.ssafy.drcha.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/auth/status")
    public ResponseEntity<?> getVerificationStatus() {
        return ResponseEntity.ok(memberService.getVerificationStatusByEmail(""));
    }
}
