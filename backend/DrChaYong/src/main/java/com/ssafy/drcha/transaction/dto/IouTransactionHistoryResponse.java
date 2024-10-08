package com.ssafy.drcha.transaction.dto;

import com.ssafy.drcha.iou.entity.Iou;
import com.ssafy.drcha.transaction.entity.TransactionHistory;
import com.ssafy.drcha.transaction.entity.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IouTransactionHistoryResponse {
    private String creditorName;
    private String debtorName;
    private LocalDateTime transactionStartDate;
    private LocalDateTime repaymentDate;
    private List<TransactionDetail> transactions;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TransactionDetail {
        private Long transactionId;
        private BigDecimal amount;
        private BigDecimal balanceBeforeTransaction;
        private BigDecimal balanceAfterTransaction;
        private LocalDateTime transactionDate;
        private Long transactionUniqueNo;
        private String description;
        private TransactionType transactionType;

        public static TransactionDetail from(TransactionHistory history) {
            return TransactionDetail.builder()
                                    .transactionId(history.getId())
                                    .amount(history.getAmount())
                                    .balanceBeforeTransaction(history.getBalanceBeforeTransaction())
                                    .balanceAfterTransaction(history.getBalanceAfterTransaction())
                                    .transactionDate(history.getTransactionDate())
                                    .transactionUniqueNo(history.getTransactionUniqueNo())
                                    .description(history.getDescription())
                                    .transactionType(history.getTransactionType())
                                    .build();
        }
    }

    public static IouTransactionHistoryResponse from(Iou iou, List<TransactionHistory> histories) {
        return IouTransactionHistoryResponse.builder()
                                            .creditorName(iou.getCreditor().getUsername())
                                            .debtorName(iou.getDebtor().getUsername())
                                            .transactionStartDate(iou.getContractStartDate())
                                            .repaymentDate(iou.getRepaymentDate())
                                            .transactions(histories.stream()
                                                                   .map(TransactionDetail::from)
                                                                   .collect(Collectors.toList()))
                                            .build();
    }
}