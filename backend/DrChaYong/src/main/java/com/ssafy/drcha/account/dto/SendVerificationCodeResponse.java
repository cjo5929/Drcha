package com.ssafy.drcha.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendVerificationCodeResponse {
    private String accountNo;
    private String code;
}
