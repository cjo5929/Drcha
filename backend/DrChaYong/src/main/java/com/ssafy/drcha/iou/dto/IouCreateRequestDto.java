package com.ssafy.drcha.iou.dto;

import java.time.LocalDateTime;

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
	private String contractEndDate;

	public Iou toEntity(Member creditor, Member debtor, ChatRoom chatRoom) {
		return Iou.builder()
			.creditor(creditor)
			.debtor(debtor)
			.chatRoom(chatRoom)
			.iouAmount(iouAmount)
			.interestRate(interestRate)
			.contractStartDate(LocalDateTime.now())
			.contractEndDate(LocalDateTime.parse(contractEndDate))
			.contractStatus(ContractStatus.DRAFTING)
			.notificationSchedule(0)
			.build();
	}
}