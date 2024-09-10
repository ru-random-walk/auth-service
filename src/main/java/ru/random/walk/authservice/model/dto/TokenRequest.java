package ru.random.walk.authservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenRequest {
    private String clientId;
}
