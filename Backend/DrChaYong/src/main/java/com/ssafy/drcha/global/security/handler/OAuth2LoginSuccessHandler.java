package com.ssafy.drcha.global.security.handler;

import com.ssafy.drcha.global.security.util.JwtUtil;
import com.ssafy.drcha.member.entity.Member;
import com.ssafy.drcha.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final MemberService memberService;

    @Value("${app.url.front}")
    private String redirectUri;

    /**
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Member member = processOAuth2User(oAuth2User);

        String accessToken = jwtUtil.generateToken(member.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(member.getEmail());

        jwtUtil.saveRefreshToken(member.getEmail(), refreshToken);

        setTokenCookies(response, accessToken, refreshToken);

        getRedirectStrategy().sendRedirect(request, response, redirectUri);
    }

    /**
     * @param oAuth2User
     * @return
     */
    private Member processOAuth2User(OAuth2User oAuth2User) {
        Map<String, Object> userInfo = extractKakaoUserInfo(oAuth2User);

        String name = (String) userInfo.get("name");
        String email = (String) userInfo.get("email");
        String avatarUrl = (String) userInfo.get("avatarUrl");

        return memberService.saveOrUpdateMember(name, email, avatarUrl);
    }

    //== build ==//

    /**
     * @param response
     * @param accessToken
     * @param refreshToken
     */
    private void setTokenCookies(HttpServletResponse response, String accessToken, String refreshToken) {
        ResponseCookie accessTokenCookie = createCookie("access_token", accessToken);
        ResponseCookie refreshTokenCookie = createCookie("refresh_token", refreshToken);

        response.addHeader(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
    }

    /**
     * @param name
     * @param value
     * @return
     */
    private ResponseCookie createCookie(String name, String value) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/")
                .build();
    }

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
