package com.ssafy.drcha.global.error.type;

import com.ssafy.drcha.global.error.ErrorCode;

public class AccountNotFoundException extends BusinessException {
	public AccountNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}

	public AccountNotFoundException(ErrorCode errorCode, String message) {
		super(errorCode, message);
	}

	public AccountNotFoundException(ErrorCode errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
	}
}
