package ru.random.walk.authservice.model.dto;

public record ChangeUserInfoDto(
        String fullName,
        String aboutMe
) {
}
