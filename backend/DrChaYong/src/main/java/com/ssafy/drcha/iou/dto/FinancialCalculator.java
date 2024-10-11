package com.ssafy.drcha.iou.dto;

public class FinancialCalculator {

	private FinancialCalculator() {
		// 유틸리티 클래스이므로 객체 생성 방지
	}

	/**
	 * 원리금을 계산하는 메서드 (단리 방식)
	 *
	 * @param iouAmount 원금 (숫자로 입력받아 변환)
	 * @param interestRate 이자율 (숫자로 입력받아 변환)
	 * @param months 기간 (개월 단위, 예: 1개월)
	 * @return 원금과 이자를 합한 금액 (문자열로 반환)
	 */
	public static Long calculateTotalAmount(Long iouAmount, Double interestRate, int months) {
		if (iouAmount == null || interestRate == null || months <= 0) {
			return null;
		}
		try {
			long principal = iouAmount;
			double rate = interestRate / 100;
			double period = months / 12.0;
			double interest = principal * rate * period;
			return Math.round(principal + interest);
		} catch (NumberFormatException e) {
			return null;
		}
	}
}