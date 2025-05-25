package ru.random.walk.authservice.model.dto.clients;

import com.fasterxml.jackson.annotation.JsonProperty;

public record VkUserInfoDto(
    @JsonProperty("user") VkUserDto user
) {
    public record VkUserDto(
            @JsonProperty("first_name") String firstName,
            @JsonProperty("last_name") String lastName,
            @JsonProperty("avatar") String avatar,
            @JsonProperty("email") String email
    ) { }
}
