package com.ssafy.drcha.global.error.type;

import com.ssafy.drcha.global.error.ErrorCode;

public class TokenNotFoundException extends BusinessException{
    public TokenNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public TokenNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public TokenNotFoundException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
