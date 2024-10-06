package com.ssafy.drcha.transaction.dto;

import com.ssafy.drcha.transaction.entity.VirtualAccount;
import com.ssafy.drcha.transaction.entity.VirtualAccountStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "가상 계좌 응답")
public class VirtualAccountResponse {
    @Schema(description = "가상 계좌 ID")
    private Long id;

    @Schema(description = "계좌 번호")
    private String accountNumber;

    @Schema(description = "은행 코드")
    private String bankCode;

    @Schema(description = "현재 잔액")
    private BigDecimal balance;

    @Schema(description = "총 상환 금액")
    private BigDecimal totalAmount;

    @Schema(description = "남은 상환 금액")
    private BigDecimal remainingAmount;

    @Schema(description = "계좌 상태")
    private VirtualAccountStatus status;

    public static VirtualAccountResponse of(VirtualAccount virtualAccount) {
        return VirtualAccountResponse.builder()
                 .id(virtualAccount.getId())
                 .accountNumber(virtualAccount.getAccountNumber())
                 .bankCode(virtualAccount.getBankCode())
                 .balance(virtualAccount.getBalance())
                 .totalAmount(virtualAccount.getTotalAmount())
                 .remainingAmount(virtualAccount.getRemainingAmount())
                 .status(virtualAccount.getStatus())
                 .build();
    }
}