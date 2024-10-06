package com.ssafy.drcha.global.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.drcha.global.api.header.HeaderResponse;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.messaging.handler.annotation.Header;

@Data
@Builder
public class TransferResponse {
    @JsonProperty("Header")
    private HeaderResponse headerResponse;  // 공통
    @JsonProperty("REC")
    private List<TransactionRecord> rec;    // 거래 정보 리스트

    @Data
    @Builder
    public static class TransactionRecord {
        private Long transactionUniqueNo;   // 거래고유번호
        private String accountNo;           // 계좌번호
        private String transactionDate;     // 거래일자
        private String transactionType;     // 거래유형
        private String transactionTypeName; // 거래유형명
        private String transactionAccountNo; // 이체 거래에 대한 계좌번호
    }
}