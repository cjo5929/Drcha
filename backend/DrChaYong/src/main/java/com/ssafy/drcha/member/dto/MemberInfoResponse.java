package com.ssafy.drcha.member.dto;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoResponse {
    private String username;
    private String email;
    private String avatarUrl;
    private String phoneNumber;
    private boolean isVerified;
}
