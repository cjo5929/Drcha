package com.ssafy.drcha.global.error.type;

import com.ssafy.drcha.global.error.ErrorCode;

import lombok.Getter;

@Getter
public class NeedsVerificationException extends BusinessException {

	public NeedsVerificationException(ErrorCode errorCode) {
		super(errorCode);
	}

	public NeedsVerificationException(ErrorCode errorCode, String message) {
		super(errorCode, message);
	}

	public NeedsVerificationException(ErrorCode errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
	}

}