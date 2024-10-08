package ru.random.walk.authservice.service.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.random.walk.authservice.model.dto.TokenResponse;
import ru.random.walk.authservice.model.exception.OAuth2BadRequestException;
import ru.random.walk.authservice.service.facade.TokenFacade;
import ru.random.walk.authservice.service.oauth2.providers.OAuth2TokenProvider;
import ru.random.walk.authservice.service.oauth2.providers.factory.OAuth2TokenRequestFactory;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenFacadeImpl implements TokenFacade {

    private final List<OAuth2TokenRequestFactory> requestFactories;
    private final List<OAuth2TokenProvider> tokenProviders;
    private static final String GRANT_TYPE_KEY = "grant_type";

    @Override
    public TokenResponse postForToken(Map<String, Object> body, String clientId) {
        if (!body.containsKey(GRANT_TYPE_KEY)) {
            throw new OAuth2BadRequestException("Unsupported grant type");
        }
        String grantType = (String) body.get(GRANT_TYPE_KEY);

        var request = requestFactories.stream()
                .filter(factory -> factory.supports(grantType))
                .map(factory -> factory.generateRequest(clientId, body))
                .findFirst()
                .orElseThrow(() -> new OAuth2BadRequestException("Unsupported grant type"));

        return tokenProviders.stream()
                .filter(provider -> provider.supports(request.getClass()))
                .map(provider -> provider.handle(request))
                .findFirst()
                .orElseThrow(() -> new OAuth2BadRequestException("Unsupported grant type"));
    }
}
