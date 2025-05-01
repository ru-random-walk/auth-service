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
            optionalRefreshToken.get().setUser(authUser);
        }

        var refreshToken = optionalRefreshToken.get();
        refreshToken.setToken(UUID.randomUUID());
        refreshToken.setExpiresAt(LocalDateTime.now().plusDays(jwtProperties.getRefreshTokenExpireTimeInDays()));

        return refreshTokenRepository.save(refreshToken);
    }
}
