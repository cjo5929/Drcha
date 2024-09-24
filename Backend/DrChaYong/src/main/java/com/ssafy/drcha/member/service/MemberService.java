package com.ssafy.drcha.member.service;

import static com.ssafy.drcha.member.entity.Member.createMember;

import com.ssafy.drcha.global.error.ErrorCode;
import com.ssafy.drcha.global.error.type.TokenNotFoundException;
import com.ssafy.drcha.global.error.type.UserNotFoundException;
import com.ssafy.drcha.global.security.util.CookieUtil;
import com.ssafy.drcha.global.security.util.JwtUtil;
import com.ssafy.drcha.member.dto.PhoneNumberRequest;
import com.ssafy.drcha.member.entity.Member;
import com.ssafy.drcha.member.enums.MemberRole;
import com.ssafy.drcha.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public Member saveOrUpdateMember(String name, String email, String avatarUrl) {
        return memberRepository.findByEmail(email)
                .map(member -> {
                    member.changeAvatarUrl(avatarUrl);
                    return memberRepository.save(member);
                })
                .orElseGet(() ->
                        memberRepository.save(createMember(name, email, avatarUrl, MemberRole.MEMBER))
                );
    }

    @Transactional(readOnly = true)
    public boolean getVerificationStatusByEmail(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        return member.isVerified();
    }

    @Transactional
    public void savePhoneNumber(String email, PhoneNumberRequest phoneNumberRequest) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        member.savePhoneNumber(phoneNumberRequest.getPhoneNumber());
        setVerificationStatusByEmail(member);
    }

    private void setVerificationStatusByEmail(Member member) {
        member.markAsVerified();
    }

    @Transactional
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = CookieUtil.getCookieValue(request, "refresh_token");

        if (refreshToken == null) {
            throw new TokenNotFoundException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }

        String email = jwtUtil.extractEmail(refreshToken);
        long expirationTime = jwtUtil.extractExpiration(refreshToken).getTime() - System.currentTimeMillis();

        jwtUtil.addToBlacklist(refreshToken, expirationTime);
        jwtUtil.deleteRefreshToken(email);
        return CookieUtil.deleteAuthTokens(request, response);
    }
}
