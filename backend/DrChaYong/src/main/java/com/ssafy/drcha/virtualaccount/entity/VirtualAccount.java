package com.ssafy.drcha.virtualaccount.entity;

import com.ssafy.drcha.global.basetime.BaseTimeEntity;
import com.ssafy.drcha.iou.entity.Iou;
import com.ssafy.drcha.member.entity.Member;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "virtual_account")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class VirtualAccount extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "virtual_account_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iou_id", nullable = false)
    private Iou iou;  // 연관된 차용증. 각 가상계좌는 하나의 차용증과 연결됨

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creditor_id", nullable = false)
    private Member creditor;  // 채권자 정보. 돈을 빌려준 사람

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "debtor_id", nullable = false)
    private Member debtor;  // 채무자 정보. 돈을 빌린 사람

    @Column(name = "account_number", nullable = false, length = 20)
    private String accountNumber;  // 가상계좌 번호. 실제 거래에 사용되는 고유 번호

    @Column(name = "bank_code", nullable = false, length = 3)
    private String bankCode;  // 은행 코드. 가상계좌를 발급한 은행 식별

    @Column(name = "balance", nullable = false)
    private BigDecimal balance;  // 현재 잔액. 실시간 계좌 잔액 추적

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;  // 총 상환해야 할 금액. 차용증의 원금과 이자 합계

    @Column(name = "remaining_amount", nullable = false)
    private BigDecimal remainingAmount;  // 남은 상환 금액. 총액에서 현재까지 상환한 금액을 뺀 값

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private VirtualAccountStatus status;  // 가상계좌의 상태 (활성, 완료, 해지 등)


    // ======== 차용증과의 연관관계 메서드 ======== //
    // 연관관계 설정을 위한 메서드
    public void linkIou(Iou iou) {
        this.iou = iou;
    }
}
