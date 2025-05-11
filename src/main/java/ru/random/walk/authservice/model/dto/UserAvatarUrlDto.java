package ru.random.walk.authservice.model.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserAvatarUrlDto(
        UUID userId,
        Long avatarVersion,
        String avatarUrl,
        LocalDateTime expiresAt
) {
}
