package com.ssafy.drcha.global.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.drcha.global.enums.SuccessCode;
import com.ssafy.drcha.global.error.ErrorCode;
import com.ssafy.drcha.global.error.response.ErrorResponse;
import com.ssafy.drcha.global.error.type.TokenNotFoundException;
import com.ssafy.drcha.global.error.type.TokenNotValidException;
import com.ssafy.drcha.global.security.service.CustomUserDetailsService;
import com.ssafy.drcha.global.security.util.CookieUtil;
import com.ssafy.drcha.global.security.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;
    private final CustomUserDetailsService customUserDetailsService;

    /**
     * 내부 필터 메서드: 각 요청에 대해 JWT 인증을 처리
     *
     * @param request  현재 HTTP 요청
     * @param response 현재 HTTP 응답
     * @param chain    다음 필터로 요청을 전달하기 위한 필터 체인
     * @throws ServletException 서블릿 처리 중 발생할 수 있는 예외
     * @throws IOException      입출력 작업 중 발생할 수 있는 예외
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        try {
            String accessToken = CookieUtil.getCookieValue(request, "access_token");

            if (accessToken != null) {
                processToken(request, response, accessToken);
            }

            chain.doFilter(request, response);
        } catch (TokenNotFoundException | TokenNotValidException e) {
            log.error(e.getMessage());
            handleException(response, e);
        }
    }

    /**
     * 액세스 토큰을 처리하고 필요한 경우 인증을 설정
     *
     * @param request     현재 HTTP 요청
     * @param response    현재 HTTP 응답
     * @param accessToken 처리할 JWT 액세스 토큰
     */
    private void processToken(HttpServletRequest request, HttpServletResponse response, String accessToken) {
        String email = jwtUtil.extractEmail(accessToken);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

            if (jwtUtil.validateToken(accessToken, userDetails)) {
                log.info(SuccessCode.ACCESS_TOKEN_VALID.getMessage());
                setAuthentication(request, userDetails);
            } else {
                log.info(ErrorCode.ACCESS_TOKEN_INVALID.getMessage());
                handleInvalidAccessToken(request, response, email, userDetails);
            }
        }
    }

    /**
     * 유효하지 않은 액세스 토큰을 처리합니다. 리프레시 토큰이 유효한 경우 새로운 액세스 토큰을 생성하고, 그렇지 않으면 예외를 던집니다.
     *
     * @param request     HTTP 요청
     * @param response    HTTP 응답
     * @param email    사용자 이름
     * @param userDetails 사용자 상세 정보
     * @throws TokenNotFoundException 리프레시 토큰이 없거나 유효하지 않은 경우
     */
    private void handleInvalidAccessToken(HttpServletRequest request, HttpServletResponse response, String email,
                                          UserDetails userDetails) {
        String refreshToken = CookieUtil.getCookieValue(request, "refresh_token");
        if (refreshToken == null) {
            log.error(ErrorCode.REFRESH_TOKEN_NOT_FOUND.getMessage());
            throw new TokenNotFoundException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }

        if (!jwtUtil.validateRefreshToken(refreshToken, email)) {
            log.error(ErrorCode.REFRESH_TOKEN_INVALID.getMessage());
            jwtUtil.deleteRefreshToken(refreshToken);
            throw new TokenNotValidException(ErrorCode.REFRESH_TOKEN_INVALID);
        }

        String newAccessToken = jwtUtil.generateToken(email);
        setNewAccessTokenCookie(response, newAccessToken);
        setAuthentication(request, userDetails);
    }

    //== build ==//

    /**
     * 현재 요청에 대한 인증을 설정
     *
     * @param request     현재 HTTP 요청
     * @param userDetails 인증에 사용될 사용자 상세 정보
     */
    private void setAuthentication(HttpServletRequest request, UserDetails userDetails) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    /**
     * 새로운 액세스 토큰을 쿠키로 설정
     *
     * @param response       현재 HTTP 응답
     * @param newAccessToken 설정할 새 액세스 토큰
     */
    private void setNewAccessTokenCookie(HttpServletResponse response, String newAccessToken) {
        ResponseCookie newAccessTokenCookie = ResponseCookie.from("access_token", newAccessToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .maxAge(jwtUtil.extractExpiration(newAccessToken).getTime())
                .path("/")
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, newAccessTokenCookie.toString());
    }

    /**
     * exception 시 httpResponse 에 401 실어서 보내기
     *
     * @param response not valid, not found
     * @param e        TokenNotFoundException | TokenNotValidException
     * @throws IOException write 오류
     */
    private void handleException(HttpServletResponse response, Exception e) throws IOException {
        ErrorCode errorCode;
        if (e instanceof TokenNotFoundException) {
            errorCode = ErrorCode.REFRESH_TOKEN_NOT_FOUND;
        } else if (e instanceof TokenNotValidException) {
            errorCode = ErrorCode.REFRESH_TOKEN_INVALID;
        } else {
            errorCode = ErrorCode.SYSTEM_ERROR;
        }

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(ErrorResponse.of(errorCode.getCode(), errorCode.getMessage())));
    }
}