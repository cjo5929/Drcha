package com.ssafy.drcha.iou.dto;

import java.time.LocalDateTime;

import com.ssafy.drcha.iou.entity.Iou;
import com.ssafy.drcha.iou.enums.ContractStatus;
import com.ssafy.drcha.member.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class IouCreateResponseDTO {
	private Long iouAmount; // 차용증 금액
	private Double interestRate; // 이자율
	private String contractEndDate; // 계약 종료 날짜

	public Iou toEntity(Member creditor, Member debtor) {
		return Iou.builder()
			.creditor(creditor)
			.debtor(debtor)
			.iouAmount(iouAmount)
			.interestRate(interestRate)
			.contractStartDate(LocalDateTime.now())
			.contractEndDate(LocalDateTime.parse(contractEndDate))
			.contractStatus(ContractStatus.DRAFTING)
			.build();
	}

}
