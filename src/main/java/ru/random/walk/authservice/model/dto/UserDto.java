package ru.random.walk.authservice.model.dto;

import java.util.UUID;

public record UserDto(
        UUID id,
        String fullName,
        String avatar,
        String description,
        Long avatarVersion
) {
}
