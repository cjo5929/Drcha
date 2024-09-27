package com.ssafy.drcha.global.error;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public enum ErrorCode {
	/**
	 * default
	 */
	SYSTEM_ERROR(INTERNAL_SERVER_ERROR, "SYSTEM-000", "서비스에 장애가 발생했습니다."),
	BAD_REQUEST_ERROR(BAD_REQUEST, "SYSTEM-001", "유효하지 않은 요청입니다."),

	/**
	 * Member
	 */
	MEMBER_FORBIDDEN_ERROR(FORBIDDEN, "MEMBER-000", "사용자의 접근 권한이 없습니다."),
	MEMBER_NOT_FOUND(NOT_FOUND, "MEMBER-001", "사용자가 존재하지 않습니다."),

	/**
	 * Token
	 */
	ACCESS_TOKEN_INVALID(UNAUTHORIZED, "TOKEN-001", "유효하지 않은 토큰입니다."),
	ACCESS_TOKEN_NOT_FOUND(NOT_FOUND, "TOKEN-002", "토큰을 찾을 수 없습니다."),
	REFRESH_TOKEN_INVALID(UNAUTHORIZED, "TOKEN-003", "유효하지 않은 리프레시 토큰입니다."),
	REFRESH_TOKEN_NOT_FOUND(NOT_FOUND, "TOKEN-004", "리프레시 토큰을 찾을 수 없습니다."),

	/**
	 * Account
	 */
	ACCOUNT_NOT_FOUND(NOT_FOUND, "ACCOUNT-001", "계좌가 존재하지 않습니다."),
	;

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;

	ErrorCode(final HttpStatus httpStatus, final String code, final String message) {
		this.httpStatus = httpStatus;
		this.code = code;
		this.message = message;
	}
}
