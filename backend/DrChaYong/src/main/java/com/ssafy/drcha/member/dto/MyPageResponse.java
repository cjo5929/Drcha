package com.ssafy.drcha.member.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageResponse {
    private String username;
    private String email;
    private String avatarUrl;
    private boolean isVerified;

    private String accountNo;
    private BigDecimal balance;
}
