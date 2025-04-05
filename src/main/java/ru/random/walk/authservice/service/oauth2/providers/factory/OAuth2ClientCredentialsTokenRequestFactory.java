package ru.random.walk.authservice.service.oauth2.providers.factory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.random.walk.authservice.model.dto.token.ClientCredentialsTokenRequest;
import ru.random.walk.authservice.model.dto.token.TokenRequest;
import ru.random.walk.authservice.service.OAuthClientService;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth2ClientCredentialsTokenRequestFactory implements OAuth2TokenRequestFactory{

    public static final String CLIENT_CREDENTIALS_GRANT_TYPE = "client_credentials";

    private final OAuthClientService clientService;

    @Override
    public boolean supports(String grantType) {
        return CLIENT_CREDENTIALS_GRANT_TYPE.equals(grantType);
    }

    @Override
    public TokenRequest generateRequest(String clientId, Map<String, Object> body) {
        return ClientCredentialsTokenRequest.builder()
                .clientId(clientId)
                .scopes(clientService.getScopesById(clientId))
                .build();
    }
}
