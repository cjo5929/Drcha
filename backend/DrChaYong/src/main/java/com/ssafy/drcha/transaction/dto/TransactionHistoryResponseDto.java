package com.ssafy.drcha.transaction.dto;

import com.ssafy.drcha.transaction.entity.TransactionHistory;
import com.ssafy.drcha.transaction.entity.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * * 거래내역 리스트 반환 시 필요한 결과값
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionHistoryResponseDto {
    private Long transactionId;
    private BigDecimal amount;
    private BigDecimal balanceBeforeTransaction;
    private BigDecimal balanceAfterTransaction;
    private LocalDateTime transactionDate;
    private Long transactionUniqueNo;
    private String creditorName;
    private String debtorName;
    private String description;
    private TransactionType transactionType;

    public static TransactionHistoryResponseDto from(TransactionHistory history) {
        return TransactionHistoryResponseDto.builder()
                .transactionId(history.getId())
                .amount(history.getAmount())
                .balanceBeforeTransaction(history.getBalanceBeforeTransaction())
                .balanceAfterTransaction(history.getBalanceAfterTransaction())
                .transactionDate(history.getTransactionDate())
                .transactionUniqueNo(history.getTransactionUniqueNo())
                .creditorName(history.getCreditorName())
                .debtorName(history.getDebtorName())
                .description(history.getDescription())
                .transactionType(history.getTransactionType())
                .build();
    }
}