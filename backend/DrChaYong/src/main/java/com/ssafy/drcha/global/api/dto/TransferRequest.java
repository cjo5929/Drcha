package com.ssafy.drcha.global.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.drcha.global.api.header.HeaderRequest;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransferRequest {
    @JsonProperty("Header")
    private HeaderRequest headerRequest;
    private String depositAccountNo;            // 입금 계좌번호
    private String withdrawalAccountNo;         // 출금 계좌번호
    private Long transactionBalance;            // 거래 금액
    private String depositTransactionSummary;   // 거래 요약 내용 (입금계좌기준)
    private String withdrawalTransactionSummary;// 출금 요약 내용 (출금계좌기준)
}