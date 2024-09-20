package com.ssafy.drcha.member.service;

import com.ssafy.drcha.global.error.ErrorCode;
import com.ssafy.drcha.global.error.type.UserNotFoundException;
import com.ssafy.drcha.member.entity.Member;
import com.ssafy.drcha.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public boolean getVerificationStatusByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        return member.isVerified();
    }
}
