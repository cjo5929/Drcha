package com.ssafy.drcha.member.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageResponse {
    private String username;
    private String email;
    private String avatarUrl;
    private boolean isVerified;
    private Long memberId;
    private String accountNo;
    private BigDecimal balance;
}
