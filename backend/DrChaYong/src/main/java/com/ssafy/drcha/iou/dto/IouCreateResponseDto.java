package com.ssafy.drcha.iou.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class IouCreateResponseDto {
	private String iouId;              // Long -> String으로 변경
	private String creditorName;
	private String debtorName;
	private String iouAmount;          // Long -> String으로 변경
	private String contractStartDate;  // LocalDateTime -> String으로 변경
	private String contractEndDate;    // LocalDateTime -> String으로 변경
	private String interestRate;       // Double -> String으로 변경
	private Boolean borrowerAgreement;
	private Boolean lenderAgreement;
	private String totalAmount;        // Long -> String으로 변경

	public static IouCreateResponseDto from(IouCreateRequestDto requestDto) {
		return new IouCreateResponseDto(
			null, // iouId는 아직 생성되지 않았으므로 null
			null, // creditorName은 추후 추가
			null, // debtorName은 추후 추가
			requestDto.getIouAmount(),
			LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
			requestDto.getContractEndDate(),
			requestDto.getInterestRate(),
			null, // borrowerAgreement는 아직 정의되지 않음
			null, // lenderAgreement는 아직 정의되지 않음
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
			long principal = Long.parseLong(iouAmount.replaceAll("[^\\d]", "")); // "500만원" -> 숫자 추출
			double rate = Double.parseDouble(interestRate);
			double period = months / 12.0;
			double interest = principal * rate * period;
			return String.valueOf(Math.round(principal + interest));
		} catch (NumberFormatException e) {
			return null; // 숫자 변환 실패 시 null 반환
		}
	}
}
