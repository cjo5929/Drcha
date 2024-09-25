package com.ssafy.drcha.global.util.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDemandDepositResponse {
    private String accountTypeUniqueNo;
    private String bankCode;
    private String bankName;
    private String accountTypeCode;
    private String accountTypeName;
    private String accountName;
    private String accountDescription;
    private String accountType;
}