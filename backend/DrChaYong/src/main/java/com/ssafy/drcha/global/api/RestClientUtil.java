package com.ssafy.drcha.global.api;

import com.ssafy.drcha.global.api.dto.*;
import com.ssafy.drcha.global.api.header.HeaderRequest;
import com.ssafy.drcha.global.api.header.HeaderResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestClientUtil {

    @Value("${api.institutionCode}")
    private String institutionCode;

    @Value("${api.fintechAppNo}")
    private String fintechAppNo;

    @Value("${api.apiKey}")
    private String apiKey;

    private static final String BASE = "https://finopenapi.ssafy.io/ssafy/api/v1/";
    private final RestClient restClient = RestClient.create();
    private final String ACCOUNT_TYPE_UNIQUE_NO = "999-1-1caa8e4919db47"; // 싸피은행 상품
    private final String DR_CHA = "차용박사";
    private final String SUCCESS_CODE = "H0000";

    // 2.2.1 사용자 계정 생성
    public UserResponse createUser(String userId) {
        String apiName = "member/";

        UserRequest request = UserRequest.builder()
                .apiKey(apiKey)
                .userId(userId)
                .build();

        return executePost(apiName, request, UserResponse.class).getBody();
    }

    // 2.2.2 사용자 계정 조회
    public UserResponse searchUser(String userId) {
        String apiName = "member/search";

        UserRequest request = UserRequest.builder()
                .apiKey(apiKey)
                .userId(userId)
                .build();

        return executePost(apiName, request, UserResponse.class).getBody();
    }

    // 2.4.1 상품 등록
    public CreateDemandDepositResponse createDemandDeposit(String userKey) {
        String prefix = "edu/demandDeposit/";
        String apiName = "createDemandDeposit";

        CreateDemandDepositRequest request = CreateDemandDepositRequest.builder()
                .headerRequest(createHeader(apiName, userKey))
                .bankCode("999")
                .accountName("")
                .accountDescription("")
                .build();

        return executePost(prefix + apiName, request, CreateDemandDepositResponse.class).getBody();
    }

    // 2.4.3 계좌 생성
    public CreateDemandDepositAccountResponse createDemandDepositAccount(String userKey) {
        String prefix = "edu/demandDeposit/";
        String apiName = "createDemandDepositAccount";

        CreateDemandDepositAccountRequest request = CreateDemandDepositAccountRequest.builder()
                .headerRequest(createHeader(apiName, userKey))
                .accountTypeUniqueNo(ACCOUNT_TYPE_UNIQUE_NO)
                .build();

        return executePost(prefix + apiName, request, CreateDemandDepositAccountResponse.class).getBody();
    }

    // 2.9.1 1원 송금 요청
    public OpenAccountAuthResponse openAccountAuth(String userKey, String accountNo) {
        String prefix = "edu/accountAuth/";
        String apiName = "openAccountAuth";

        OpenAccountAuthRequest request = OpenAccountAuthRequest.builder()
                .headerRequest(createHeader(apiName, userKey))
                .accountNo(accountNo)
                .authText(DR_CHA)
                .build();

        return executePost(prefix + apiName, request, OpenAccountAuthResponse.class).getBody();
    }

    // 2.4.13 계좌 거래 내역 조회 (단건)
    public InquireTransactionHistoryResponse inquireTransactionHistory(String userKey, String accountNo, Long transactionUniqueNo) {
        String prefix = "edu/demandDeposit/";
        String apiName = "inquireTransactionHistory";

        InquireTransactionHistoryRequest request = InquireTransactionHistoryRequest.builder()
                .headerRequest(createHeader(apiName, userKey))
                .accountNo(accountNo)
                .transactionUniqueNo(transactionUniqueNo)
                .build();

        return executePost(prefix + apiName, request, InquireTransactionHistoryResponse.class).getBody();
    }

    // API 호출용 HeaderRequest 생성
    public HeaderRequest createHeader(String apiName, String userKey) {
        LocalDateTime today = LocalDateTime.now();

        String date = today.toString().split("T")[0].replace("-", "");
        String time = today.toString().split("T")[1].substring(0, 8).replace(":", "");

        String institutionTransactionUniqueNo = date + time + generateNumericUUID().substring(0, 6);

        return HeaderRequest.builder()
                .apiName(apiName)
                .transmissionDate(date)
                .transmissionTime(time)
                .institutionCode(institutionCode)
                .fintechAppNo(fintechAppNo)
                .apiServiceCode(apiName)
                .institutionTransactionUniqueNo(institutionTransactionUniqueNo)
                .apiKey(apiKey)
                .userKey(userKey)
                .build();
    }

    // 정수형 UUID 생성
    private static String generateNumericUUID() {
        return java.util.UUID.randomUUID().toString().replaceAll("[^0-9]", "");
    }

    private <T> ResponseEntity<T> executePost(String apiName, Object requestBody, Class<T> responseType) {
        return restClient.post()
                .uri(BASE + apiName)
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .toEntity(responseType);
    }

    public void validateApiResponse(HeaderResponse headerResponse) {
        String responseCode = headerResponse.getResponseCode();

        if (!SUCCESS_CODE.equals(responseCode)) {
            throw new RuntimeException("은행 API 요청 실패");
        }
    }
}
