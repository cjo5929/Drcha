package com.ssafy.drcha.iou.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.ssafy.drcha.chat.entity.ChatRoom;
import com.ssafy.drcha.iou.entity.Iou;
import com.ssafy.drcha.iou.enums.ContractStatus;
import com.ssafy.drcha.member.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class IouCreateRequestDto {
	private Long iouAmount;
	private Double interestRate;
	private LocalDateTime contractEndDate;

	// 정적 팩토리 메서드를 추가하여 String 값을 적절히 변환하도록 함
	public static IouCreateRequestDto fromStrings(String iouAmountStr, String interestRateStr, String contractEndDateStr) {
		Long iouAmount = 0L;
		try {
			if (iouAmountStr != null && !iouAmountStr.trim().isEmpty()) {
				iouAmount = Long.parseLong(iouAmountStr.replace(",", "").trim());
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid value for iouAmount: " + iouAmountStr, e);
		}

		Double interestRate = 0.0;
		try {
			if (interestRateStr != null && !interestRateStr.trim().isEmpty()) {
				interestRate = Double.parseDouble(interestRateStr.replace("%", "").trim());
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid value for interestRate: " + interestRateStr, e);
		}

		LocalDateTime contractEndDate = LocalDateTime.now();
		try {
			if (contractEndDateStr != null && !contractEndDateStr.trim().isEmpty()) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
				contractEndDate = LocalDateTime.parse(contractEndDateStr, formatter);
			}
		} catch (DateTimeParseException e) {
			throw new IllegalArgumentException("Invalid value for contractEndDate: " + contractEndDateStr, e);
		}

		return new IouCreateRequestDto(iouAmount, interestRate, contractEndDate);
	}

	// 기존의 toEntity 메서드
	public Iou toEntity(Member creditor, Member debtor, ChatRoom chatRoom) {
		// 기본값 설정을 간단하게 처리
		Long finalIouAmount = (iouAmount != null) ? iouAmount : 0L;
		Double finalInterestRate = (interestRate != null) ? interestRate : 0.0;
		LocalDateTime finalContractEndDate = (contractEndDate != null) ? contractEndDate : LocalDateTime.now();

		return Iou.builder()
			.creditor(creditor)
			.debtor(debtor)
			.chatRoom(chatRoom)
			.iouAmount(finalIouAmount)
			.interestRate(finalInterestRate)
			.contractStartDate(LocalDateTime.now())
			.contractEndDate(finalContractEndDate)
			.contractStatus(ContractStatus.DRAFTING)
			.notificationSchedule(0)
			.build();
	}
}
