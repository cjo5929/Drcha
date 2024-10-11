package com.ssafy.drcha.global.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.drcha.global.api.header.HeaderRequest;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CloseAccountRequest {
    @JsonProperty("Header")
    private HeaderRequest headerRequest;  // 공통
    private String accountNo;             // 해지할 계좌번호
    private String refundAccountNo;       // 금액반환 계좌번호 (잔액이 0원이 아닌 경우 필수) -> 잔액이 0원이면 미입력 가능
}