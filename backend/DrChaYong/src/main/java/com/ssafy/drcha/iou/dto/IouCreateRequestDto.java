package com.ssafy.drcha.iou.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import com.ssafy.drcha.chat.entity.ChatRoom;
import com.ssafy.drcha.iou.entity.Iou;
import com.ssafy.drcha.iou.enums.ContractStatus;
import com.ssafy.drcha.member.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class IouCreateRequestDto {
	private String iouAmount;
	private String interestRate;
	private String contractEndDate;

	public Iou toEntity(Member creditor, Member debtor, ChatRoom chatRoom) {

		Long finalIouAmount = 0L;
		try {
			if (iouAmount != null && !iouAmount.trim().isEmpty()) {
				finalIouAmount = Long.parseLong(iouAmount.replace(",", ""));
			}
		} catch (NumberFormatException e) {
			finalIouAmount = 0L;
		}

		Double finalInterestRate = 0.0;
		try {
			if (interestRate != null && !interestRate.trim().isEmpty()) {
				finalInterestRate = Double.parseDouble(interestRate.replace("%", "").trim());
			}
		} catch (NumberFormatException e) {
			finalInterestRate = 0.0;
		}

		LocalDateTime finalContractEndDate;
		try {
			if (contractEndDate != null && !contractEndDate.trim().isEmpty()) {
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일", Locale.KOREAN);
				LocalDate date = LocalDate.parse(contractEndDate, formatter);
				finalContractEndDate = date.atStartOfDay();
			} else {
				finalContractEndDate = LocalDateTime.now();
			}
		} catch (DateTimeParseException e) {
			finalContractEndDate = LocalDateTime.now();
		}

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