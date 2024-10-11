package com.ssafy.drcha.global.error.type;

import com.ssafy.drcha.global.error.ErrorCode;

public class IouNotFoundException extends BusinessException {


    public IouNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

    public IouNotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public IouNotFoundException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
