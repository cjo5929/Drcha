package com.ssafy.drcha.iou.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class IouCreateResponseDto {
	private String iouId;
	private String creditorName;
	private String debtorName;
	private String iouAmount;
	private String contractStartDate;
	private String contractEndDate;
	private String interestRate;
	private Boolean borrowerAgreement;
	private Boolean lenderAgreement;
	private String totalAmount;

	public static IouCreateResponseDto from(IouCreateRequestDto requestDto, String creditorName, String debtorName) {
		return new IouCreateResponseDto(
			null,
			creditorName,
			debtorName,
			requestDto.getIouAmount(),
			LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
			requestDto.getContractEndDate(),
			requestDto.getInterestRate(),
			false,
			false,
			calculateTotalAmount(requestDto.getIouAmount(), requestDto.getInterestRate(), 12)
		);
	}

	/**
	 * 원리금을 계산하는 메서드 (단리 방식)
	 *
	 * @param iouAmount 원금 (문자열로 입력받아 변환)
	 * @param interestRate 이자율 (문자열로 입력받아 변환)
	 * @param months 기간 (개월 단위, 예: 1개월)
	 * @return 원금과 이자를 합한 금액 (문자열로 반환)
	 */
	private static String calculateTotalAmount(String iouAmount, String interestRate, int months) {
		if (iouAmount == null || interestRate == null || months <= 0) {
			return null;
		}
		try {
			long principal = Long.parseLong(iouAmount.replaceAll("[^\\d]", ""));
			double rate = Double.parseDouble(interestRate) / 100;
			double period = months / 12.0;
			double interest = principal * rate * period;
			return String.valueOf(Math.round(principal + interest));
		} catch (NumberFormatException e) {
			return null;
		}
	}
}
