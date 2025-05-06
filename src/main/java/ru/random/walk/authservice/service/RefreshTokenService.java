package ru.random.walk.authservice.service;

import ru.random.walk.authservice.model.entity.AuthUser;
import ru.random.walk.authservice.model.entity.RefreshTokenEntity;
import ru.random.walk.authservice.model.exception.AuthAuthorizationException;

import java.util.UUID;

public interface RefreshTokenService {
    /**
     * Find existing token info by token name
     *
     * @param token token name
     * @return existing RefreshTokenEntity
     * @throws AuthAuthorizationException if token does not exist
     */
    RefreshTokenEntity getRefreshToken(String token);

    /**
     * Create new refresh token if it does not exist, or recreate existing token
     *
     * @param authUser user entity
     * @return a new RefreshTokenEntity
     */
    RefreshTokenEntity refreshTokenForUser(AuthUser authUser);

    /**
     * Removes refresh token if it exists
     * @param userId user id
     */
    void removeTokenForUser(UUID userId);
}
