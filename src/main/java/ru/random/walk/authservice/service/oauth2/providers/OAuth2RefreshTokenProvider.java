package ru.random.walk.authservice.service.oauth2.providers;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.random.walk.authservice.model.dto.token.RefreshTokenRequest;
import ru.random.walk.authservice.model.dto.token.TokenRequest;
import ru.random.walk.authservice.model.dto.TokenResponse;
import ru.random.walk.authservice.model.entity.RefreshTokenEntity;
import ru.random.walk.authservice.model.exception.AuthAuthorizationException;
import ru.random.walk.authservice.service.JwtService;
import ru.random.walk.authservice.service.RefreshTokenService;

import java.time.LocalDateTime;


@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth2RefreshTokenProvider implements OAuth2TokenProvider {

    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;


    @Override
    public boolean supports(Class<? extends TokenRequest> clazz) {
        return clazz == RefreshTokenRequest.class;
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
            throw new AuthAuthorizationException("Token expired");
        }
    }
}
