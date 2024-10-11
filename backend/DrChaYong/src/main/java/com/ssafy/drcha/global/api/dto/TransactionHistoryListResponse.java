package com.ssafy.drcha.global.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.drcha.global.api.header.HeaderResponse;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TransactionHistoryListResponse {
    private HeaderResponse headerResponse;  // 공통
    @JsonProperty("REC")
    private REC rec;                        // 거래 내역 정보
    
    @Data
    @Builder
    public static class REC {
        private String totalCount;          // 조회총건수
        private List<Transaction> list;     // 거래목록

        @Data
        @Builder
        public static class Transaction { // ! 거래별
            private Long transactionUniqueNo;      // 거래고유번호
            private String transactionDate;        // 거래일자
            private String transactionTime;        // 거래시각
            private String transactionType;        // 입금출금구분 (1:입금, 2:출금)
            private String transactionTypeName;    // 입금출금구분명
            private String transactionAccountNo;   // 거래계좌번호
            private Long transactionBalance;       // 거래금액
            private Long transactionAfterBalance;  // 거래후잔액
            private String transactionSummary;     // 거래요약내용
            private String transactionMemo;        // 거래메모
        }
    }
}