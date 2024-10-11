package com.ssafy.drcha.global.error.type;

import com.ssafy.drcha.global.error.ErrorCode;

public class VirtualAccountException extends BusinessException {
    public VirtualAccountException(ErrorCode errorCode) {
        super(errorCode);
    }

    public VirtualAccountException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public VirtualAccountException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}