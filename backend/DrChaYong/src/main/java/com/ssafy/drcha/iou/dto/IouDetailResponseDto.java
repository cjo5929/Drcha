package com.ssafy.drcha.iou.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ssafy.drcha.iou.entity.Iou;
import com.ssafy.drcha.iou.enums.ContractStatus;
import com.ssafy.drcha.member.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class IouDetailResponseDto {

	private Long iouId;
	private String creditorName;
	private String debtorName;
	private Long iouAmount;
	private LocalDateTime contractStartDate;
	private LocalDateTime contractEndDate;
	private Double interestRate;
	private Boolean borrowerAgreement;
	private Boolean lenderAgreement;
	private ContractStatus contractStatus;
	private Long totalAmount;
	private Integer notificationSchedule;
	private String phoneNumber;
	private BigDecimal iouBalance;
	private Long daysUntilDue;


	public static IouDetailResponseDto from(Iou iou, Member member, Long daysUntilDue) {
		return new IouDetailResponseDto(
			iou.getIouId(),
			iou.getCreditor().getUsername(),
			iou.getDebtor().getUsername(),
			iou.getIouAmount(),
			iou.getContractStartDate(),
			iou.getContractEndDate(),
			iou.getInterestRate(),
			iou.getBorrowerAgreement(),
			iou.getLenderAgreement(),
			iou.getContractStatus(),
			FinancialCalculator.calculateTotalAmount(iou.getIouAmount(), iou.getInterestRate(), 12),
			iou.getNotificationSchedule(),
			member.getPhoneNumber(),
			iou.getBalance(),
			daysUntilDue
			);
	}


}