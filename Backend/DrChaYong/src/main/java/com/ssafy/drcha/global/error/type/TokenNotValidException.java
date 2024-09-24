package com.ssafy.drcha.global.error.type;

import com.ssafy.drcha.global.error.ErrorCode;


public class TokenNotValidException extends BusinessException{

    public TokenNotValidException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TokenNotValidException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public TokenNotValidException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
