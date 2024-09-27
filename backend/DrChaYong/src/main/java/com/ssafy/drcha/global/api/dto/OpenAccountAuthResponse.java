package com.ssafy.drcha.global.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.drcha.global.api.header.HeaderResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OpenAccountAuthResponse {
    @JsonProperty("Header")
    private HeaderResponse header;

    @JsonProperty("REC")
    private Rec rec;

    @Data
    @Builder
    public static class Rec {
        private Long transactionUniqueNo;
        private String accountNo;
    }
}
