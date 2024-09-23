package com.ssafy.drcha.global.enums;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum SuccessCode {
    /**
     * Member
     */
    LOGIN_SUCCESS(OK, "MEMBER-001", "로그인 성공했습니다."),

    /**
     * Token
     */
    ACCESS_TOKEN_VALID(UNAUTHORIZED, "TOKEN-001", "유효한 토큰입니다."),
    REFRESH_TOKEN_VALID(UNAUTHORIZED, "TOKEN-002", "유효한 리프레시 토큰입니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    SuccessCode(final HttpStatus httpStatus, final String code, final String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}