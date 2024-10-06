package com.ssafy.drcha.iou.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class IouCreateResponseDto {
	private Long iouId;
	private String creditorName;
	private String debtorName;
	private Long iouAmount;
	private LocalDateTime contractStartDate;
	private LocalDateTime contractEndDate;
	private Double interestRate;
	private Boolean borrowerAgreement;
	private Boolean lenderAgreement;
	private Long totalAmount;

	public static IouCreateResponseDto from(IouCreateRequestDto requestDto, String creditorName, String debtorName) {
		return new IouCreateResponseDto(
			null,
			creditorName,
			debtorName,
			requestDto.getIouAmount(),
			LocalDateTime.now(),
			requestDto.getContractEndDate(),
			requestDto.getInterestRate(),
			false,
			false,
			FinancialCalculator.calculateTotalAmount(requestDto.getIouAmount(), requestDto.getInterestRate(), 12)
		);
	}

}
