package com.ssafy.drcha.transaction.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DepositRequestDto {
    private String accountNo;
    private Long transactionBalance;
    private String transactionSummary;
}