package ru.random.walk.authservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.random.walk.authservice.config.security.jwt.JwtProperties;
import ru.random.walk.authservice.model.entity.AuthUser;
import ru.random.walk.authservice.model.entity.RefreshTokenEntity;
import ru.random.walk.authservice.model.exception.AuthAuthorizationException;
import ru.random.walk.authservice.repository.RefreshTokenRepository;
import ru.random.walk.authservice.service.RefreshTokenService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;

    @Override
    public RefreshTokenEntity getRefreshToken(String token) {
        return refreshTokenRepository.findByToken(UUID.fromString(token))
                .orElseThrow(() -> new AuthAuthorizationException("Invalid token"));
    }

    @Override
    @Transactional
    public RefreshTokenEntity refreshTokenForUser(AuthUser authUser) {
        var optionalRefreshToken = refreshTokenRepository.findById(authUser.getId());

        if (optionalRefreshToken.isEmpty()) {
            optionalRefreshToken = Optional.of(new RefreshTokenEntity());
        }

        var refreshToken = optionalRefreshToken.get();
        refreshToken.setUserId(authUser.getId());
        refreshToken.setToken(UUID.randomUUID());
        refreshToken.setExpiresAt(LocalDateTime.now().plusDays(jwtProperties.getRefreshTokenExpireTimeInDays()));

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    @Transactional
    public void removeTokenForUser(UUID userId) {
        var optionalRefreshToken = refreshTokenRepository.findById(userId);

        if (optionalRefreshToken.isEmpty()) {
            log.info("No refresh token exists for user {}", userId);
            return;
        }

        var refreshToken = optionalRefreshToken.get();
        refreshTokenRepository.delete(refreshToken);
        log.info("Refresh token is deleted for user {}", userId);
    }
}
