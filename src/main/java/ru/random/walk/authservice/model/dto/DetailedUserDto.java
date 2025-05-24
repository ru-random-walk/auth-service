package ru.random.walk.authservice.model.dto;

import java.util.UUID;

public record DetailedUserDto(
        UUID id,
        String fullName,
        String email,
        String avatar,
        String description,
        Long avatarVersion
) {
}
