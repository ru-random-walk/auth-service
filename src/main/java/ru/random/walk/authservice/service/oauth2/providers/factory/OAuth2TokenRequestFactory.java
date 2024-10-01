package ru.random.walk.authservice.service.oauth2.providers.factory;

import ru.random.walk.authservice.model.dto.token.TokenRequest;

import java.util.Map;

public interface OAuth2TokenRequestFactory {
    boolean supports(String grantType);
    TokenRequest generateRequest(String clientId, Map<String, Object> body);
}
