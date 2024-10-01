package com.ssafy.drcha.iou.dto;

import java.time.LocalDateTime;

import com.ssafy.drcha.iou.entity.Iou;
import com.ssafy.drcha.iou.enums.ContractStatus;

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

	public static IouTransactionResponseDto from(Iou iou) {
		return new IouTransactionResponseDto(
			iou.getIouId(),
			iou.getDebtor().getUsername(),
			iou.getIouAmount(),
			iou.getContractStartDate(),
			iou.isBothAgreed(),
			iou.getContractStatus()
		);
	}
}