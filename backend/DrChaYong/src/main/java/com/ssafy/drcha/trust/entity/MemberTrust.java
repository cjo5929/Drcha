package com.ssafy.drcha.trust.entity;

import com.ssafy.drcha.global.basetime.BaseTimeEntity;
import com.ssafy.drcha.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberTrust extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trustId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "current_late_trades")
    private int currentLateTrades;

    @Column(name = "current_debt_trades")
    private int currentDebtTrades;

    @Column(name = "completed_trades")
    private int completedTrades;

    //== 생성 메서드 ==//
    public static MemberTrust initializeMemberTrust(Member member) {
        return MemberTrust.builder()
                .member(member)
                .currentLateTrades(0)
                .currentDebtTrades(0)
                .completedTrades(0)
                .build();
    }

    //== 비즈니스 로직 ==//
    public void initMember(Member member){
        this.member = member;
    }

    /**
     * 현재 진행 중인 채무 거래 수를 1 증가시킵니다.
     */
    public void incrementCurrentDebtTrades() {
        this.currentDebtTrades++;
    }

    /**
     * 채무 거래를 연체 거래로 전환합니다.
     * 현재 채무 거래 수를 1 감소시키고, 연체 거래 수를 1 증가시킵니다.
     */
    public void convertDebtTradeToLateTrade() {
        if (this.currentDebtTrades > 0) {
            this.currentDebtTrades--;
            this.currentLateTrades++;
        }
    }

    /**
     * 채무 거래를 완료 처리합니다.
     * 현재 채무 거래 수를 1 감소시키고, 완료된 거래 수를 1 증가시킵니다.
     */
    public void completeDebtTrade() {
        if (this.currentDebtTrades > 0) {
            this.currentDebtTrades--;
            this.completedTrades++;
        }
    }

    /**
     * 연체 거래를 완료 처리합니다.
     * 현재 연체 거래 수를 1 감소시키고, 완료된 거래 수를 1 증가시킵니다.
     */
    public void completeLateTrade() {
        if (this.currentLateTrades > 0) {
            this.currentLateTrades--;
            this.completedTrades++;
        }
    }
}