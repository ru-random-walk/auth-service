package ru.random.walk.authservice.model.dto.clients;

import com.fasterxml.jackson.annotation.JsonProperty;

public record YandexUserInfoDto(
        @JsonProperty("first_name") String firstName,
        @JsonProperty("last_name") String lastName,
        @JsonProperty("default_avatar_id") String avatarId,
        @JsonProperty("is_avatar_empty") Boolean isAvatarEmpty,
        @JsonProperty("default_email") String email
) {
}
