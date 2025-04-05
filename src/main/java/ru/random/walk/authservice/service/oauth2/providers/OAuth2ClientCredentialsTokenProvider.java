package ru.random.walk.authservice.service.oauth2.providers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.random.walk.authservice.model.dto.TokenResponse;
import ru.random.walk.authservice.model.dto.token.ClientCredentialsTokenRequest;
import ru.random.walk.authservice.model.dto.token.TokenRequest;
import ru.random.walk.authservice.service.JwtService;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2ClientCredentialsTokenProvider implements OAuth2TokenProvider {

    private final JwtService jwtService;

    @Override
    public boolean supports(Class<? extends TokenRequest> clazz) {
        return clazz == ClientCredentialsTokenRequest.class;
    }

    @Override
    public TokenResponse handle(TokenRequest tokenRequest) {
        return jwtService.generateToken(tokenRequest, null);
    }
}
