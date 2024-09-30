package com.ssafy.drcha.iou.dto;

import java.time.LocalDateTime;

import com.ssafy.drcha.iou.entity.Iou;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class IouResponseDto {

	private Long iouId;
	private String creditorName;
	private String debtorName;
	private Long iouAmount;
	private LocalDateTime contractStartDate;
	private LocalDateTime contractEndDate;
	private Double interestRate;
	private Boolean borrowerAgreement;
	private Boolean lenderAgreement;
	private Long totalAmount;  // 원리금 합산 필드


	public static IouResponseDto from(Iou iou) {
		return IouResponseDto.builder()
			.iouId(iou.getIouId())
			.creditorName(iou.getCreditor().getUsername())
			.debtorName(iou.getDebtor().getUsername())
			.iouAmount(iou.getIouAmount())
			.contractStartDate(iou.getContractStartDate())
			.contractEndDate(iou.getContractEndDate())
			.interestRate(iou.getInterestRate())
			.borrowerAgreement(iou.getBorrowerAgreement())
			.lenderAgreement(iou.getLenderAgreement())
			.totalAmount(calculateTotalAmount(iou.getIouAmount(), iou.getInterestRate()))  // 원리금 계산하여 추가
			.build();
	}

	/**
	 * 원리금을 계산하는 메서드
	 *
	 * @param iouAmount 원금
	 * @param interestRate 이자율 (예: 0.05는 5% 이자율을 의미)
	 * @return 원금과 이자를 합한 금액
	 */
	private static Long calculateTotalAmount(Long iouAmount, Double interestRate) {
		if (iouAmount == null || interestRate == null) {
			return null;
		}
		double interest = iouAmount * interestRate;
		return Math.round(iouAmount + interest);  // 원리금 합산 후 반올림하여 반환
	}
}