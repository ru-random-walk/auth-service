package ru.random.walk.authservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.random.walk.authservice.config.security.jwt.JwtProperties;
import ru.random.walk.authservice.model.entity.AuthUser;
import ru.random.walk.authservice.model.entity.RefreshTokenEntity;
import ru.random.walk.authservice.model.exception.OAuth2AuthorizationException;
import ru.random.walk.authservice.repository.RefreshTokenRepository;
import ru.random.walk.authservice.service.RefreshTokenService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProperties jwtProperties;

    @Override
    public RefreshTokenEntity getRefreshToken(String token) {
        return refreshTokenRepository.findById(UUID.fromString(token))
                .orElseThrow(() -> new OAuth2AuthorizationException("Invalid token"));
    }

    @Override
    @Transactional
    public RefreshTokenEntity refreshTokenForUser(AuthUser authUser) {
        if (refreshTokenRepository.existsByUser(authUser)) {
            log.info("Refresh token for user {} already exists. Deleting it", authUser.getId());
            refreshTokenRepository.deleteByUser(authUser);
        }

        RefreshTokenEntity entity = new RefreshTokenEntity();
        entity.setUser(authUser);
        entity.setExpiresAt(LocalDateTime.now().plusDays(jwtProperties.getRefreshTokenExpireTimeInDays()));

        return refreshTokenRepository.save(entity);
    }
}
