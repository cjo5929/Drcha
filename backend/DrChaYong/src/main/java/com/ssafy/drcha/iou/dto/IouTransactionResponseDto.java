package com.ssafy.drcha.iou.dto;

import java.time.LocalDateTime;

import com.ssafy.drcha.iou.entity.Iou;
import com.ssafy.drcha.iou.enums.ContractStatus;
import com.ssafy.drcha.member.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public final class IouTransactionResponseDto {

	private Long iouId;
	private String opponentName;
	private Long principalAmount;
	private LocalDateTime contractStartDate;
	private Boolean agreementStatus;
	private ContractStatus contractStatus;

	// 상대방의 이름을 결정하기 위해 수정된 from 메서드
	public static IouTransactionResponseDto from(Iou iou, Member member) {
		String opponentName = iou.getCreditor().equals(member) ? iou.getDebtor().getUsername() : iou.getCreditor().getUsername();

		return new IouTransactionResponseDto(
			iou.getIouId(),
			opponentName,
			iou.getIouAmount(),
			iou.getContractStartDate(),
			iou.isBothAgreed(),
			iou.getContractStatus()
		);
	}
}
