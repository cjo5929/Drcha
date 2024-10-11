package com.ssafy.drcha.global.security.strategy;

import com.ssafy.drcha.member.entity.Member;

public interface RedirectStrategy {
    String getRedirectUrl(Member member, String frontendUrl, String chatRoomId);
}
