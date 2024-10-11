package com.ssafy.drcha.global.api.header;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HeaderResponse {
    private String responseCode;
    private String responseMessage;
    private String apiName;
    private String transmissionDate;
    private String transmissionTime;
    private String institutionCode;
    private String apiKey;
    private String apiServiceCode;
    private String institutionTransactionUniqueNo;
}
