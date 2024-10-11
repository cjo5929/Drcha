package com.ssafy.drcha.global.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.drcha.global.api.header.HeaderRequest;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionHistoryListRequest {
    @JsonProperty("Header")
    private HeaderRequest headerRequest;  // 공통
    private String accountNo;             // 계좌번호
    private String startDate;             // 조회시작일자
    private String endDate;               // 조회종료일자
    private String transactionType;       // 거래구분 (M:입금, D:출금, A:전체)
    private String orderByType;           // 정렬순서
}