package com.ssafy.drcha.transaction.entity;

import com.ssafy.drcha.global.basetime.BaseTimeEntity;
import com.ssafy.drcha.iou.entity.Iou;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transaction_history")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TransactionHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iou_id", nullable = false)
    private Iou iou;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false)
    private TransactionType transactionType;    // 거래 유형 (입금, 출금, 이체)

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;   // 거래 금액

    @Column(name = "balance_before_transaction", nullable = false)
    private BigDecimal balanceBeforeTransaction;   //거래 전 차용증 잔액

    @Column(name = "balance_after_transaction", nullable = false)
    private BigDecimal balanceAfterTransaction;   // 거래 후 차용증 잔액

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;       // 거래 일시

    @Column(name = "transaction_unique_no", nullable = false, unique = true)
    private Long transactionUniqueNo;   // 고유 거래 번호

    @Column(name = "creditor_name", nullable = false)
    private String creditorName;  // 채권자 이름

    @Column(name = "debtor_name", nullable = false)
    private String debtorName;    // 채무자 이름

    @Column(name = "description")
    private String description;   // ! 거래 설명 또는 메모 작성 시

}