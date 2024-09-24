package com.ssafy.drcha.iou.entity;

import java.time.LocalDateTime;
import com.ssafy.drcha.global.basetime.BaseTimeEntity;
import com.ssafy.drcha.iou.enums.ContractStatus;
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
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "iou")
@Getter
@Builder
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
	private Long iouAmount;

	@Column(name = "contract_start_date", nullable = false)
	private LocalDateTime contractStartDate;

	@Column(name = "contract_end_date", nullable = false)
	private LocalDateTime contractEndDate;

	@Column(name = "interest_rate", nullable = false)
	private Double interestRate;

	@Column(name = "contract_status", nullable = false)
	@Enumerated(EnumType.STRING)
	private ContractStatus contractStatus;

	@Column(name = "chat_room_id", nullable = false)
	private Long chatRoomId;

	@Column(name = "notification_schedule", nullable = false)
	private Integer notificationSchedule;

	public static Iou createIou(Member creditor, Member debtor, Long iouAmount,
		LocalDateTime contractStartDate, LocalDateTime contractEndDate,
		Double interestRate, ContractStatus contractStatus,
		Long chatRoomId, Integer notificationSchedule) {

		return Iou.builder()
			.creditor(creditor)
			.debtor(debtor)
			.iouAmount(iouAmount)
			.contractStartDate(contractStartDate)
			.contractEndDate(contractEndDate)
			.interestRate(interestRate)
			.contractStatus(contractStatus)
			.chatRoomId(chatRoomId)
			.notificationSchedule(notificationSchedule)
			.build();

	}


	public void updateContractStatus(ContractStatus newStatus) {
		this.contractStatus = newStatus;
	}
}