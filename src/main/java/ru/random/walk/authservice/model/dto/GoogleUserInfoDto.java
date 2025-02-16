package ru.random.walk.authservice.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder(toBuilder = true)
public record GoogleUserInfoDto (

    String name,

    @JsonProperty("given_name")
    String givenName,

    @JsonProperty("family_name")
    String familyName,

    String email,
    String picture

) {}
