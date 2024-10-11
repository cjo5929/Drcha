package com.ssafy.drcha.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CorsConfig implements WebMvcConfigurer {

    @Value("${app.url.front}")
    private String frontUrl;

    @Value("${ai.llm.service-url}")
    private String llmServiceUrl;


    /**
     * CORS(Cross-Origin Resource Sharing) 설정
     * 프론트엔드에서 백엔드 API 에 접근할 수 있도록 CORS 설정을 적용
     *
     * @param registry CorsRegistry 객체
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedOrigins(frontUrl, llmServiceUrl)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials", "Set-Cookie",
                        "Access-Token")
                .allowCredentials(true)
                .maxAge(1800);
    }
}