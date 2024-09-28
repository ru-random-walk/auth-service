package ru.random.walk.authservice.model.dto;

import lombok.Builder;

import java.util.List;

@Builder(toBuilder = true)
public record JwkKeyResponse (
    List<JwkKeyDto> keys
) {}
