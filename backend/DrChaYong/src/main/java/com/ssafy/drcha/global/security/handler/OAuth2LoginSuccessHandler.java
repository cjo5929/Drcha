package com.ssafy.drcha.global.security.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.ssafy.drcha.global.security.strategy.RedirectStrategy;
import com.ssafy.drcha.global.security.strategy.RedirectStrategyFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.ssafy.drcha.account.service.AccountService;
import com.ssafy.drcha.global.api.RestClientUtil;
import com.ssafy.drcha.global.api.dto.UserResponse;
import com.ssafy.drcha.global.security.util.JwtUtil;
import com.ssafy.drcha.member.entity.Member;
import com.ssafy.drcha.member.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final MemberService memberService;
    private final RestClientUtil restClientUtil;
    private final AccountService accountService;
    private final RedirectStrategyFactory redirectStrategyFactory;

    @Value("${app.url.front}")
    private String frontendUrl;

    /**
     * OAuth2 인증 성공 시 호출되는 메서드
     * 사용자 정보를 처리하고 JWT 토큰을 생성하여 쿠키에 저장한 후 리다이렉트
     *
     * @param request 현재 HTTP 요청
     * @param response 현재 HTTP 응답
     * @param authentication 인증 정보
     * @throws IOException 리다이렉트 중 발생할 수 있는 예외
     */

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Member member = processOAuth2User(oAuth2User);
        log.info("---- {} 님 로그인 성공했습니다. ----", member.getUsername());

        String accessToken = jwtUtil.generateToken(member.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(member.getEmail());

        jwtUtil.saveRefreshToken(member.getEmail(), refreshToken);

        setTokenCookies(response, accessToken, refreshToken);

        String chatRoomId = request.getParameter("state");
        log.info("---- chatRoomId {} 가 넙어 왔습니다 ----", chatRoomId);

        RedirectStrategy strategy = redirectStrategyFactory.getStrategy(chatRoomId);
        String redirectUrl = strategy.getRedirectUrl(member, frontendUrl, chatRoomId);

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    /**
     * OAuth2User 객체에서 필요한 사용자 정보를 추출하고 처리
     *
     * @param oAuth2User OAuth2 인증으로 받아온 사용자 정보
     * @return 처리된 Member 객체
     */
    private Member processOAuth2User(OAuth2User oAuth2User) {
        Map<String, Object> userInfo = extractKakaoUserInfo(oAuth2User);

        String name = (String) userInfo.get("name");
        String email = (String) userInfo.get("email");
        String avatarUrl = (String) userInfo.get("avatarUrl");

        // 1. 사용자 계정 조회 (싸피 은행 API)
        UserResponse userResponse = null;
        try {
            userResponse = restClientUtil.searchUser(email);
        } catch (Exception e) {
            log.info("사용자 계정이 존재하지 않음, 새로운 계정을 생성합니다: {}", email);
        }

        String userKey;
        Member member = null;
        // 2. 사용자 계정이 없으면 createUser 호출하여 생성
        if (userResponse == null) {
            userResponse = restClientUtil.createUser(email);
            log.info("새로운 사용자 계정이 생성되었습니다. userKey: {}", userResponse.getUserKey());
            userKey = userResponse.getUserKey();
            member = memberService.saveOrUpdateMember(name, email, avatarUrl, userKey);
        } else {
            member = memberService.saveOrUpdateMember(name, email, avatarUrl, userResponse.getUserKey());
            log.info("기존 사용자 계정을 찾았습니다. userKey: {}", userResponse.getUserKey());
        }

        try {
            log.info("회원가입 중 이메일 -> {}", email);
            accountService.saveNewBankAccount(email);
            log.info("새로운 계좌가 생성되었습니다: {}", email);
        } catch (Exception e) {
            log.error("계좌 생성 중 오류가 발생했습니다: {}", e.getMessage());
        }

        // 3. 얻은 userKey를 저장한 Member 객체 반환
        return member;
    }

    //== build ==//

    /**
     * 응답에 액세스 토큰과 리프레시 토큰을 쿠키로 설정
     *
     * @param response HTTP 응답 객체
     * @param accessToken 생성된 액세스 토큰
     * @param refreshToken 생성된 리프레시 토큰
     */
    private void setTokenCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        ResponseCookie accessTokenCookie = createCookie("access_token", accessToken);
        ResponseCookie refreshTokenCookie = createCookie("refresh_token", refreshToken);

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }

    /**
     * 주어진 이름과 값으로 보안 쿠키를 생성
     *
     * @param name 쿠키 이름
     * @param value 쿠키 값
     * @return 생성된 ResponseCookie 객체
     */
    private ResponseCookie createCookie(String name, String value) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .build();
    }

    /**
     * OAuth2User 객체에서 카카오 사용자 정보를 추출
     *
     * @param oAuth2User OAuth2 인증으로 받아온 사용자 정보
     * @return 추출된 카카오 사용자 정보를 담은 Map 객체
     */
    private Map<String, Object> extractKakaoUserInfo(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", profile.get("nickname"));
        userInfo.put("email", kakaoAccount.get("email"));
        userInfo.put("avatarUrl", profile.get("profile_image_url"));

        return userInfo;
    }
}
