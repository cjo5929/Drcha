
package com.ssafy.drcha.global.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.HttpSessionOAuth2AuthorizationRequestRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.ssafy.drcha.global.security.filter.CustomAuthorizationRequestResolver;
import com.ssafy.drcha.global.security.filter.JwtAuthenticationFilter;
import com.ssafy.drcha.global.security.handler.OAuth2LoginSuccessHandler;
import com.ssafy.drcha.member.enums.Role;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final ClientRegistrationRepository clientRegistrationRepository;


    @Value("${app.url.front}")
    private String frontUrl;

    @Value("${ai.llm.service-url}")
    private String llmServiceUrl;

    @Bean
    public HttpSessionOAuth2AuthorizationRequestRepository authorizationRequestRepository() {
        return new HttpSessionOAuth2AuthorizationRequestRepository();
    }


    /**
     * Spring Security 의 보안 필터 체인을 정의
     * CORS 설정, CSRF 비활성화, 요청 인증 설정, 세션 관리 정책, OAuth2 로그인 설정 등을 포함
     *
     * @param http HttpSecurity 객체
     * @return SecurityFilterChain 객체
     * @throws Exception 설정 과정에서 발생할 수 있는 예외
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        CustomAuthorizationRequestResolver customAuthorizationRequestResolver =
            new CustomAuthorizationRequestResolver(clientRegistrationRepository);

        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/login","/api/v1/member/logout", "/error", "/oauth2/**",
                        "/h2-console", "/swagger-ui/index.html", "/swagger",
                        "/swagger-ui.html", "/swagger-ui/**", "/api-docs", "/api-docs/**",
                        "/api/swagger-ui/index.html", "/api/swagger",
                        "/api/swagger-ui.html", "/api/swagger-ui/**", "/api/api-docs", "/api/api-docs/**",
                        "/ws/**", "/sub/**", "/pub/**")
                    .permitAll()
                    .requestMatchers("/api/v1/**").hasAuthority(Role.MEMBER.name())
                    .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .authorizationEndpoint(authorizationEndpoint ->
                    authorizationEndpoint
                        .authorizationRequestResolver(customAuthorizationRequestResolver)
                        .authorizationRequestRepository(authorizationRequestRepository())
                       .baseUri("/oauth2/authorization"))
//                         .baseUri("/api/oauth2/authorization"))
                .redirectionEndpoint(redirectionEndpoint ->
                    redirectionEndpoint
                       .baseUri("/login/oauth2/code/*"))
//                         .baseUri("/api/login/oauth2/code/*"))
                .successHandler(oAuth2LoginSuccessHandler)
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * CORS(Cross-Origin Resource Sharing) 설정을 정의
     * 특정 도메인에서 오는 요청을 허용하고, 허용되는 HTTP 메소드와 헤더를 설정
     *
     * @return CorsConfigurationSource 객체
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(frontUrl, llmServiceUrl));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        // WebSocket을 위한 설정 추가
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

