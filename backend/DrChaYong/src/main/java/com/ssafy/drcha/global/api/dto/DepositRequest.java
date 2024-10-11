package com.ssafy.drcha.global.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.drcha.global.api.header.HeaderRequest;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepositRequest {
    @JsonProperty("Header")
    private HeaderRequest headerRequest; // 공통
    private String accountNo;            // 계좌번호
    private Long transactionBalance;     // 입금금액
    private String transactionSummary;   // 입금계좌요약
}