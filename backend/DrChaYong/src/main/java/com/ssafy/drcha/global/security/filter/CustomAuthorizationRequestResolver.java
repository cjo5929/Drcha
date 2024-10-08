package com.ssafy.drcha.global.security.filter;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

	private final OAuth2AuthorizationRequestResolver defaultAuthorizationRequestResolver;

	public CustomAuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository) {
		this.defaultAuthorizationRequestResolver =
			new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/api/oauth2/authorization");
	}

	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
		OAuth2AuthorizationRequest authorizationRequest = defaultAuthorizationRequestResolver.resolve(request);
		return authorizationRequest != null ? customizeAuthorizationRequest(request, authorizationRequest) : null;
	}

	@Override
	public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
		OAuth2AuthorizationRequest authorizationRequest = defaultAuthorizationRequestResolver.resolve(request, clientRegistrationId);
		return authorizationRequest != null ? customizeAuthorizationRequest(request, authorizationRequest) : null;
	}

	private OAuth2AuthorizationRequest customizeAuthorizationRequest(HttpServletRequest request, OAuth2AuthorizationRequest authorizationRequest) {
		Map<String, Object> additionalParameters = new HashMap<>(authorizationRequest.getAdditionalParameters());

		// HttpServletRequest에서 custom_state 값을 가져옵니다.
		String customState = request.getParameter("custom_state");
		log.info("===========custom_state: {}", customState);

		if (customState != null) {
			additionalParameters.put("custom_state", customState);
			// 세션에 custom_state 저장
			request.getSession().setAttribute("custom_state", customState);
		}

		return OAuth2AuthorizationRequest.from(authorizationRequest)
			.additionalParameters(additionalParameters)
			.state(authorizationRequest.getState()) // 기존 state는 그대로 사용
			.build();
	}

}