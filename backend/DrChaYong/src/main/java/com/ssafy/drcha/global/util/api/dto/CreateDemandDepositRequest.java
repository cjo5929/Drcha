package com.ssafy.drcha.global.util.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ssafy.drcha.global.util.api.header.Header;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateDemandDepositRequest {
    @JsonProperty("Header")
    private Header header;
    private String bankCode;
    private String accountName;
    private String accountDescription;
}
