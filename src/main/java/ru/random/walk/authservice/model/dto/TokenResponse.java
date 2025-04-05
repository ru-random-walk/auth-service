package ru.random.walk.authservice.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TokenResponse (
        @Schema(description = "Token to access resource", example = "eyJ0eXAiOiJKV1QiLCJraWQiOiJyd19rZXk")
        @JsonProperty("access_token") String accessToken,

        @Schema(description = "Token to refresh access_token", example = "07880a4b-1677-41fd-b53d-1401eb8a3802")
        @JsonProperty("refresh_token") String refreshToken,

        @Schema(description = "Type of access_token", example = "Bearer")
        @JsonProperty("token_type") String tokenType,

        @Schema(description = "Access token ttl in seconds", example = "900")
        @JsonProperty("expires_in") Long expiresIn
) {}
