package com.ssafy.drcha.iou.dto;

import java.time.LocalDateTime;

import com.ssafy.drcha.iou.entity.Iou;
import com.ssafy.drcha.iou.enums.ContractStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class IouResponseDto {

	private Long iouId;
	private Long iouAmount;
	private LocalDateTime contractStartDate;
	private LocalDateTime contractEndDate;
	private Double interestRate;
	private Long totalAmount;
	private ContractStatus contractStatus;
	private boolean borrowerAgreement;
	private boolean lenderAgreement;


	public static IouResponseDto from(Iou iou) {
		return new IouResponseDto(
			iou.getIouId(),
			iou.getIouAmount(),
			iou.getContractStartDate(),
			iou.getContractEndDate(),
			iou.getInterestRate(),
			FinancialCalculator.calculateTotalAmount(iou.getIouAmount(), iou.getInterestRate(), 12),
			iou.getContractStatus(),
			iou.getBorrowerAgreement(),
			iou.getLenderAgreement()
		);
	}


}