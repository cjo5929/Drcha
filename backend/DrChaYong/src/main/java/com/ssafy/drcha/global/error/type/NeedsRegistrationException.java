package com.ssafy.drcha.global.error.type;

import com.ssafy.drcha.global.error.ErrorCode;

import lombok.Getter;

@Getter
public class NeedsRegistrationException extends BusinessException {

	public NeedsRegistrationException(ErrorCode errorCode) {
		super(errorCode);
	}

	public NeedsRegistrationException(ErrorCode errorCode, String message) {
		super(errorCode, message);
	}

	public NeedsRegistrationException(ErrorCode errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
	}

}
