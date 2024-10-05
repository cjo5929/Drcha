package com.ssafy.drcha.trust.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberTrustInfoResponse {
    private final int lateTrades;
    private final int debtTrades;
    private final String message;

    public static MemberTrustInfoResponse withTradeHistory(int lateTrades, int debtTrades, String message) {
        return MemberTrustInfoResponse.builder()
                .lateTrades(lateTrades)
                .debtTrades(debtTrades)
                .message(message)
                .build();
    }
}