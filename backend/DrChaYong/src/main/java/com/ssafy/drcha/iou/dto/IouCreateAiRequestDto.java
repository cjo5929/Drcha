package com.ssafy.drcha.iou.dto;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class IouCreateAiRequestDto {
	private String iouAmount;
	private String interestRate;
	private String contractEndDate;

	// 문자열 데이터를 IouCreateRequestDto로 변환하는 메서드
	public IouCreateRequestDto toIouCreateRequestDto() {
		Long finalIouAmount = 0L;
		try {
			if (iouAmount != null && !iouAmount.trim().isEmpty()) {
				finalIouAmount = Long.parseLong(iouAmount.replace(",", "").trim());
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid value for iouAmount: " + iouAmount, e);
		}

		Double finalInterestRate = 0.0;
		try {
			if (interestRate != null && !interestRate.trim().isEmpty()) {
				finalInterestRate = Double.parseDouble(interestRate.replace("%", "").trim());
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid value for interestRate: " + interestRate, e);
		}

		LocalDateTime finalContractEndDate = LocalDateTime.now();
		try {
			if (contractEndDate != null && !contractEndDate.trim().isEmpty()) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				finalContractEndDate = LocalDateTime.parse(contractEndDate, formatter);
			}
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("Invalid value for contractEndDate: " + contractEndDate, e);
		}

		return new IouCreateRequestDto(finalIouAmount, finalInterestRate, finalContractEndDate);
	}
}
