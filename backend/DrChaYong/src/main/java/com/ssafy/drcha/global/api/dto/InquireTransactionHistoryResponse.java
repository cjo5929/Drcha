package com.ssafy.drcha.global.util.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.drcha.global.util.api.header.HeaderResponse;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class InquireTransactionHistoryResponse {
    @JsonProperty("Header")
    private HeaderResponse header;

    @JsonProperty("REC")
    private Rec rec;

    @Data
    @Builder
    public static class Rec {
        private String transactionUniqueNo;
        private String transactionDate;
        private String transactionTime;
        private String transactionType;
        private String transactionTypeName;
        private String transactionAccountNo;
        private String transactionBalance;
        private String transactionAfterBalance;
        private String transactionSummary;
        private String transactionMemo;
    }
}
