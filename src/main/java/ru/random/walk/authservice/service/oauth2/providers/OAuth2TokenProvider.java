package ru.random.walk.authservice.service.oauth2.providers;

import ru.random.walk.authservice.model.dto.token.TokenRequest;
import ru.random.walk.authservice.model.dto.TokenResponse;

import java.util.Map;

public interface OAuth2TokenProvider {
    boolean supports(String grantType);
    TokenRequest generateRequest(String clientId, Map<String, Object> body);
    TokenResponse handle(TokenRequest tokenRequest);
}
