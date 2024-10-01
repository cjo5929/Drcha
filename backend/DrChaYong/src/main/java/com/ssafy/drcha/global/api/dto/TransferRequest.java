package com.ssafy.drcha.global.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.drcha.global.api.header.HeaderRequest;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransferRequest {
    @JsonProperty("Header")
    private HeaderRequest headerRequest;    // 공통
    private String withdrawalAccountNo;     // 출금계좌번호
    private String depositAccountNo;        // 입금계좌번호
    private Long transactionBalance;        // 거래금액
    private String transactionSummary;      // 거래요약내용
}