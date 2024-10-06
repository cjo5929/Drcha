package com.ssafy.drcha.global.security.strategy;

import com.ssafy.drcha.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class ChatRoomRedirectStrategy implements RedirectStrategy{
    @Override
    public String getRedirectUrl(Member member, String frontendUrl, String chatRoomId) {
        if (member.isVerified()) {
            return frontendUrl + "/chat/" + chatRoomId;
        }

        return frontendUrl + "/auth/account";
    }
}
