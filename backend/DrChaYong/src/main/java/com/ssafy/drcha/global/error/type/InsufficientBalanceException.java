package com.ssafy.drcha.global.error.type;

import com.ssafy.drcha.global.error.ErrorCode;

public class InsufficientBalanceException extends BusinessException{

    public InsufficientBalanceException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InsufficientBalanceException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public InsufficientBalanceException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
