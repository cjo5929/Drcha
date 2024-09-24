package com.ssafy.drcha.global.error.type;

import com.ssafy.drcha.global.error.ErrorCode;

public class UserNotFoundException extends BusinessException {
	public UserNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}

	public UserNotFoundException(ErrorCode errorCode, String message) {
		super(errorCode, message);
	}

	public UserNotFoundException(ErrorCode errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
	}
}
