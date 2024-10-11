package com.ssafy.drcha.member.service;

import static com.ssafy.drcha.member.entity.Member.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ssafy.drcha.account.entity.Account;
import com.ssafy.drcha.account.repository.AccountRepository;
import com.ssafy.drcha.global.error.ErrorCode;
import com.ssafy.drcha.global.error.type.AccountNotFoundException;
import com.ssafy.drcha.global.error.type.TokenNotFoundException;
import com.ssafy.drcha.global.error.type.UserNotFoundException;
import com.ssafy.drcha.global.security.util.CookieUtil;
import com.ssafy.drcha.global.security.util.JwtUtil;
import com.ssafy.drcha.member.dto.MyPageResponse;
import com.ssafy.drcha.member.dto.PhoneNumberRequest;
import com.ssafy.drcha.member.entity.Member;
import com.ssafy.drcha.member.enums.Role;
import com.ssafy.drcha.member.repository.MemberRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final AccountRepository accountRepository;

    @Transactional
    public Member saveOrUpdateMember(String name, String email, String avatarUrl, String userKey) {
        return memberRepository.findByEmail(email)
                .map(member -> {
                    member.changeAvatarUrl(avatarUrl);
                    member.changeUserKey(userKey);
                    return memberRepository.save(member);
                })
                .orElseGet(() ->
                        memberRepository.save(createMember(name, email, avatarUrl, Role.MEMBER, userKey))
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

    @Transactional(readOnly = true)
    public MyPageResponse getMemberInfo(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(ErrorCode.MEMBER_NOT_FOUND));
        Account account = accountRepository.findByMember(member)
                .orElseThrow(() -> new AccountNotFoundException(ErrorCode.ACCOUNT_NOT_FOUND));

        return MyPageResponse.builder()
                .username(member.getUsername())
                .email(member.getEmail())
                .avatarUrl(member.getAvatarUrl())
                .isVerified(member.isVerified())
                .accountNo(account.getAccountNumber())
                .balance(account.getBalance())
                .memberId(member.getId())
                .build();
    }
}
