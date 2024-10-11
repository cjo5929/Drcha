package com.ssafy.drcha.global.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.drcha.global.api.header.HeaderResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CloseAccountResponse {
    private HeaderResponse headerResponse;  // 공통
    @JsonProperty("REC")
    private REC rec;                        // 해지 계좌 정보
    
    @Data
    @Builder
    public static class REC {
        private String status;              // 상태 (예: "CLOSED")
        private String accountNo;           // 해지된 계좌번호
        private String refundAccountNo;     // 금액반환 계좌번호
        private Long accountBalance;        // 계좌 해지 잔액
    }
}