package com.ssafy.drcha.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
    private String bankName;
    private String accountNumber;
    private String accountHolderName;
    private BigDecimal balance;
    private boolean isPrimary;
}
