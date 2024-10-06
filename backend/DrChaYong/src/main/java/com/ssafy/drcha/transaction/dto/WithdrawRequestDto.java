package com.ssafy.drcha.transaction.dto;

import lombok.Builder;
import lombok.Data;

/**
 * * 출금 요청 시 필요한 정보
 */
@Data
@Builder
public class WithdrawRequestDto {
    private String accountNo;             // 계좌번호
    private Long transactionBalance;      // 출금금액
    private String transactionSummary;    // 출금계좌요약
}