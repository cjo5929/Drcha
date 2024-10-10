package com.ssafy.drcha.iou.dto;

import java.time.LocalDateTime;

import com.ssafy.drcha.iou.entity.Iou;

import com.ssafy.drcha.iou.enums.ContractStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public final class IouPdfResponseDto {
	private Long iouId;
	private String creditorName;
	private String debtorName;
	private Long iouAmount;
	private LocalDateTime contractStartDate;
	private LocalDateTime contractEndDate;
	private Double interestRate;
	@Setter
	private Boolean borrowerAgreement;
	@Setter
	private Boolean lenderAgreement;
	private Long totalAmount;
	private String creditorPhoneNumber;
	private String debtorPhoneNumber;
	private ContractStatus contractStatus;

	public static IouPdfResponseDto from(Iou iou) {
		return new IouPdfResponseDto(
			iou.getIouId(),
			iou.getCreditor().getUsername(),
			iou.getDebtor().getUsername(),
			iou.getIouAmount(),
			iou.getContractStartDate(),
			iou.getContractEndDate(),
			iou.getInterestRate(),
			iou.getBorrowerAgreement(),
			iou.getLenderAgreement(),
			FinancialCalculator.calculateTotalAmount(iou.getIouAmount(), iou.getInterestRate(), 12),
			iou.getCreditor().getPhoneNumber(),
			iou.getDebtor().getPhoneNumber(),
			iou.getContractStatus()
		);
	}

}