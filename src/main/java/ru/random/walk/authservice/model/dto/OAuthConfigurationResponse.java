package ru.random.walk.authservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder(toBuilder = true)
public record OAuthConfigurationResponse (
    @JsonProperty("issuer")
    String issuer,

    @JsonProperty("token_endpoint")
    String tokenEndpoint,

    @JsonProperty("jwks_uri")
    String jwksUri,

    @JsonProperty("grant_types_supported")
    List<String> supportedGrantTypes,

    @JsonProperty("response_types_supported")
    List<String> supportedResponseType
) {}
