package com.ssafy.drcha.global.api.header;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;

@Builder
@Getter
@ToString
public class HeaderRequest {
    private String apiName;
    private String transmissionDate;
    private String transmissionTime;
    @Value("${api.institutionCode}")
    private String institutionCode;
    @Value("${api.fintechAppNo}")
    private String fintechAppNo;
    private String apiServiceCode;
    private String institutionTransactionUniqueNo;
    @Value("${api.apiKey}")
    private String apiKey;
    private String userKey;
}
