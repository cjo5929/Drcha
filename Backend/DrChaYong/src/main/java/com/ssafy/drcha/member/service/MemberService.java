package com.ssafy.drcha.member.service;

import com.ssafy.drcha.global.error.ErrorCode;
import com.ssafy.drcha.global.error.type.UserNotFoundException;
import com.ssafy.drcha.member.dto.PhoneNumberRequest;
import com.ssafy.drcha.member.entity.Member;
import com.ssafy.drcha.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public boolean getVerificationStatusByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        return member.isVerified();
    }

    public void savePhoneNumber(String email, PhoneNumberRequest phoneNumberRequest) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        member.savePhoneNumber(phoneNumberRequest.getPhoneNumber());
        setVerificationStatusByEmail(member);
    }

    private void setVerificationStatusByEmail(Member member) {
        member.markAsVerified();
    }
}
