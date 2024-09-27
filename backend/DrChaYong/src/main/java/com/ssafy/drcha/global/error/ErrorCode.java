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
	 * Chat
	 */
	CHAT_ROOM_NOT_FOUND(NOT_FOUND, "CHAT-001", "채팅방을 찾을 수 없습니다."),
	CHAT_MESSAGE_NOT_FOUND(NOT_FOUND, "CHAT-002", "채팅 메시지를 찾을 수 없습니다."),
	CHAT_ROOM_ACCESS_DENIED(FORBIDDEN, "CHAT-003", "채팅방에 접근할 권한이 없습니다."),
	CHAT_ROOM_FULL(BAD_REQUEST, "CHAT-004", "채팅방이 가득 찼습니다."),
	CHAT_MESSAGE_SEND_FAILED(INTERNAL_SERVER_ERROR, "CHAT-005", "메시지 전송에 실패했습니다."),
	CHAT_INVALID_MESSAGE_TYPE(BAD_REQUEST, "CHAT-006", "유효하지 않은 메시지 타입입니다."),
	CHAT_USER_ALREADY_IN_ROOM(BAD_REQUEST, "CHAT-007", "사용자가 이미 채팅방에 참여중입니다."),
	CHAT_USER_NOT_IN_ROOM(BAD_REQUEST, "CHAT-008", "사용자가 채팅방에 참여하지 않았습니다."),


	/**
	 * Account
	 */
	ACCOUNT_NOT_FOUND(NOT_FOUND, "ACCOUNT-001", "계좌가 존재하지 않습니다.");
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
