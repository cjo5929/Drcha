package com.ssafy.drcha.global.security.strategy;

import com.ssafy.drcha.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class MainPageRedirectStrategy implements RedirectStrategy {
    @Override
    public String getRedirectUrl(Member member, String frontendUrl, String chatRoomId) {
        return frontendUrl;
    }
}