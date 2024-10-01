package com.ssafy.drcha.global.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.drcha.global.api.header.HeaderResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransferResponse {
    private HeaderResponse headerResponse;  // 공통
    @JsonProperty("REC")
    private REC rec;                        // 거래 정보

    @Data
    @Builder
    public static class REC {
        private Long transactionUniqueNo;   // 거래고유번호
        private String transactionDate;     // 거래일자
    }
}