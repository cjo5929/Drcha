package com.ssafy.drcha.global.error.type;

import com.ssafy.drcha.global.error.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
	private final ErrorCode errorCode;
	private final String code;
	private final String message;

	public BusinessException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
		this.code = errorCode.getCode();
		this.message = errorCode.getMessage();
	}

	public BusinessException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
		this.code = errorCode.getCode();
		this.message = message;
	}

	public BusinessException(ErrorCode errorCode, String message, Throwable cause) {
		super(message);
		this.errorCode = errorCode;
		this.code = errorCode.getCode();
		this.message = message;
		this.initCause(cause);
	}

}
