package com.ssafy.drcha.global.util.api;

import com.ssafy.drcha.global.util.api.header.Header;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    // API 호출용 Header 생성
    public Header createHeader(String apiName, String userKey) {
        LocalDateTime today = LocalDateTime.now();

        String date = today.toString().split("T")[0].replace("-", "");
        String time = today.toString().split("T")[1].substring(0, 8).replace(":", "");

        String institutionTransactionUniqueNo = date + time + generateNumericUUID().substring(0, 6);

        return Header.builder()
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

}
