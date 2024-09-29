package ru.random.walk.authservice.service.oauth2.providers;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.random.walk.authservice.model.dto.token.RefreshTokenRequest;
import ru.random.walk.authservice.model.dto.token.TokenRequest;
import ru.random.walk.authservice.model.dto.TokenResponse;
import ru.random.walk.authservice.model.entity.RefreshTokenEntity;
import ru.random.walk.authservice.model.exception.OAuth2AuthorizationException;
import ru.random.walk.authservice.model.exception.OAuth2BadRequestException;
import ru.random.walk.authservice.service.JwtService;
import ru.random.walk.authservice.service.RefreshTokenService;

import java.time.LocalDateTime;
import java.util.Map;


@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth2RefreshTokenProvider implements OAuth2TokenProvider {

    public static final String REFRESH_TOKEN_GRANT_TYPE = "refresh_token";
    private static final String REFRESH_TOKEN_KEY = "refresh_token";
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

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

    @Override
    @Transactional
    public TokenResponse handle(TokenRequest tokenRequest) {
        var request = (RefreshTokenRequest) tokenRequest;
        var existingToken = refreshTokenService.getRefreshToken(request.getRefreshToken());
        checkNotExpired(existingToken);
        var user = existingToken.getUser();

        return jwtService.generateToken(tokenRequest, user);
    }

    private void checkNotExpired(RefreshTokenEntity token) {
        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new OAuth2AuthorizationException("Token expired");
        }
    }
}
