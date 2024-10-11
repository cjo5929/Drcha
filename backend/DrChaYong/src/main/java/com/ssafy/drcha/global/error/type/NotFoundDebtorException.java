package com.ssafy.drcha.global.error.type;

import com.ssafy.drcha.global.error.ErrorCode;

public class NotFoundDebtorException extends BusinessException{
    public NotFoundDebtorException(ErrorCode errorCode) {
        super(errorCode);
    }

    public NotFoundDebtorException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public NotFoundDebtorException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}
