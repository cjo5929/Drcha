package com.ssafy.drcha.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.Arrays;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("차용박사 API 명세서")
                .description(
                        "<h2>특화 프로젝트</h2>" +
                                "<h3>Swagger를 이용한 API 명세서</h3><br>" +
                                "<img src=\"/images/Dr_cha_LOGO.png\" alt = '프로젝트 로고'  width=\"450\">" +
                                "<h3>프로젝트 정보</h3>" +
                                "차용박사")
                .version("v1.0.0")
                .contact(new Contact()
                        .name("조현수")
                        .email("ssafyhyunsoo@gmail.com")
                        .url("http://i11a209.p.ssafy.io:8080")
                );

        // JWT 보안 스키마 정의
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        // 전역 보안 요구사항 정의
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearerAuth", securityScheme))
                .security(Arrays.asList(securityRequirement))
                .info(info);
    }

    // ! auth 관련 API 모음
    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("auth")
                .pathsToMatch("/qufit/auth/**")
                .build();
    }

    // ! member 관련 API 모음
    @Bean
    public GroupedOpenApi memberApi() {
        return GroupedOpenApi.builder()
                .group("member")
                .pathsToMatch("/qufit/member/**")
                .build();
    }

    // ! chat 관련 API 모음
    @Bean
    public GroupedOpenApi chatApi() {
        return GroupedOpenApi.builder()
                .group("chat")
                .pathsToMatch("/qufit/chat/**")
                .build();
    }

    // ! admin 관련 API 모음
    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("admin")
                .pathsToMatch("/qufit/admin/**")
                .build();
    }

    // ! WebSocket 관련 API 모음
    public GroupedOpenApi websocketApi() {
        return GroupedOpenApi.builder()
                .group("websocket")
                .pathsToMatch("/stomp/chat")
                .build();
    }

    // ! transaction 관련 API 모음
    @Bean
    public GroupedOpenApi videoApi() {
        return GroupedOpenApi.builder()
                .group("transaction")
                .pathsToMatch("/api/transaction/**")
                .build();
    }

    @Bean
    public GroupedOpenApi friendApi() {
        return GroupedOpenApi.builder()
                .group("friend")
                .pathsToMatch("/api/friend/**")
                .build();
    }

    @Bean
    public GroupedOpenApi balanceGameApi() {
        return GroupedOpenApi.builder()
                .group("loan")
                .pathsToMatch("/api/loan/**")
                .build();
    }
}