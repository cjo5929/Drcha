package com.ssafy.drcha.trust.enums;

public enum TrustInfoMessage {
    FIRST_TIME_TRADER("첫 거래자에요"),
    WARNING_TRADER("주의하세요"),
    SAFE_TRADER("안전해요");

    private final String message;

    TrustInfoMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}