package com.ssafy.drcha.global.util.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.drcha.global.util.api.header.HeaderResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateDemandDepositAccountResponse {
    @JsonProperty("Header")
    private HeaderResponse header;

    @JsonProperty("REC")
    private Rec rec;

    @Data
    @Builder
    public static class Rec {
        private String bankCode;
        private String accountNo;
        private Currency currency;

        @Data
        @Builder
        public static class Currency {
            private String currency;
            private String currencyName;
        }
    }
}
