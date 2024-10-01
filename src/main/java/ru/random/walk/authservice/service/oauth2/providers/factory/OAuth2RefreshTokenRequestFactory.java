package ru.random.walk.authservice.service.oauth2.providers.factory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.random.walk.authservice.model.dto.token.RefreshTokenRequest;
import ru.random.walk.authservice.model.dto.token.TokenRequest;
import ru.random.walk.authservice.model.exception.OAuth2BadRequestException;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth2RefreshTokenRequestFactory implements OAuth2TokenRequestFactory{

    public static final String REFRESH_TOKEN_GRANT_TYPE = "refresh_token";
    private static final String REFRESH_TOKEN_KEY = "refresh_token";

    @Override
    public boolean supports(String grantType) {
        return REFRESH_TOKEN_GRANT_TYPE.equals(grantType);
    }

    @Override
    public TokenRequest generateRequest(String clientId, Map<String, Object> body) {
        if (!body.containsKey(REFRESH_TOKEN_KEY)) {
            throw new OAuth2BadRequestException("Invalid request");
        }

        String refreshToken = (String) body.get(REFRESH_TOKEN_KEY);

        return RefreshTokenRequest.builder()
                .clientId(clientId)
                .refreshToken(refreshToken)
                .build();
    }
}
