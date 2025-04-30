package ru.random.walk.authservice.service.facade;

import ru.random.walk.authservice.model.dto.EmailAuthDto;
import ru.random.walk.authservice.model.dto.TokenResponse;

import java.util.Map;

public interface TokenFacade {
    TokenResponse postForToken(Map<String, Object> body, String clientId);
    void issueOneTimePassword(EmailAuthDto dto);
}
