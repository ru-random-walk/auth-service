package ru.random.walk.authservice.model.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record ApiErrorDto (
        String message
) {}
