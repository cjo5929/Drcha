package com.ssafy.drcha.transaction.dto;

import lombok.Builder;
import lombok.Data;

/**
 * * 계좌 이체에 필요한 요구사항
 */
@Data
@Builder
public class TransferRequestDto {
    private String withdrawalAccountNo;
    private String depositAccountNo;
    private Long transactionBalance;
    private String depositTransactionSummary;
    private String withdrawalTransactionSummary;
}