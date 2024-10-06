package com.ssafy.drcha.global.security.strategy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedirectStrategyFactory {
    private final ChatRoomRedirectStrategy chatRoomRedirectStrategy;
    private final MainPageRedirectStrategy mainPageRedirectStrategy;

    public RedirectStrategy getStrategy(String chatRoomId) {
        return chatRoomId != null && !chatRoomId.isEmpty() ? chatRoomRedirectStrategy : mainPageRedirectStrategy;
    }
}