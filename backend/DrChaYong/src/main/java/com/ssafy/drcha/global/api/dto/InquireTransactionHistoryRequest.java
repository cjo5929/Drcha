package com.ssafy.drcha.global.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.drcha.global.api.header.HeaderRequest;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
public class InquireTransactionHistoryRequest {
    @JsonProperty("Header")
    private HeaderRequest headerRequest;

    private String accountNo;
    private Long transactionUniqueNo;
}
