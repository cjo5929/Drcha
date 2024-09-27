package com.ssafy.drcha.global.api.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequest {
    private String apiKey;
    private String userId;
}
