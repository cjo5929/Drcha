package com.ssafy.drcha.iou.entity;

import com.ssafy.drcha.chat.entity.ChatRoom;
import com.ssafy.drcha.global.basetime.BaseTimeEntity;
import com.ssafy.drcha.iou.enums.ContractStatus;
import com.ssafy.drcha.member.entity.Member;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "iou")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Iou extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "iou_id")
	private Long iouId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creditor_id", nullable = false)
	private Member creditor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "debtor_id", nullable = false)
	private Member debtor;

	@Column(name = "iou_amount", nullable = false)
	@Positive
	private Long iouAmount;

	@Column(name = "contract_start_date", nullable = false)
	private LocalDateTime contractStartDate;

	@Column(name = "contract_end_date", nullable = false)
	private LocalDateTime contractEndDate;

	@Column(name = "interest_rate", nullable = false)
	@PositiveOrZero
	private Double interestRate;

	@Enumerated(EnumType.STRING)
	@Column(name = "contract_status", nullable = false)
	private ContractStatus contractStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "chat_room_id", nullable = false)
	private ChatRoom chatRoom;

	@Column(name = "notification_schedule", nullable = false)
	@Positive
	private Integer notificationSchedule;

	@Column(name = "borrower_agreement", nullable = false)
	private Boolean borrowerAgreement;

	@Column(name = "lender_agreement", nullable = false)
	private Boolean lenderAgreement;

	@Column(name = "agreement_date")
	private LocalDateTime agreementDate;

	@OneToOne(mappedBy = "iou", cascade = CascadeType.ALL, orphanRemoval = true)
	private VirtualAccount virtualAccount;

	@Builder
	private Iou(Member creditor, Member debtor, Long iouAmount,
				LocalDateTime contractStartDate, LocalDateTime contractEndDate,
				Double interestRate, ContractStatus contractStatus,
				ChatRoom chatRoom, Integer notificationSchedule) {
		this.creditor = creditor;
		this.debtor = debtor;
		this.iouAmount = iouAmount;
		this.contractStartDate = contractStartDate;
		this.contractEndDate = contractEndDate;
		this.interestRate = interestRate;
		this.contractStatus = contractStatus;
		this.chatRoom = chatRoom;
		this.notificationSchedule = notificationSchedule;
		this.borrowerAgreement = false;
		this.lenderAgreement = false;
	}

	public boolean isBothAgreed() {
		return this.borrowerAgreement && this.lenderAgreement;
	}

	public void updateLoanInfo(Long iouAmount, Double interestRate, LocalDateTime contractEndDate) {
		this.iouAmount = iouAmount;
		this.interestRate = interestRate;
		this.contractEndDate = contractEndDate;
	}

	public void updateContractStatus(ContractStatus newStatus) {
		this.contractStatus = newStatus;
	}

	public void borrowerAgree() {
		this.borrowerAgreement = true;
		this.checkAndSetAgreementDate();
	}

	public void lenderAgree() {
		this.lenderAgreement = true;
		this.checkAndSetAgreementDate();
	}

	private void checkAndSetAgreementDate() {
		if (this.borrowerAgreement && this.lenderAgreement && this.agreementDate == null) {
			this.agreementDate = LocalDateTime.now();
			this.contractStatus = ContractStatus.ACTIVE;
		}
	}

	// ======== 가상계좌와의 연관관계 메서드 ======= //
	public void linkVirtualAccount(VirtualAccount virtualAccount) {
		this.virtualAccount = virtualAccount;
		if (virtualAccount != null && virtualAccount.getIou() != this) {
			virtualAccount.linkIou(this);
		}
	}
}
