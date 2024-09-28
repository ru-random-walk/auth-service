package ru.random.walk.authservice.service.facade;

import jakarta.servlet.http.HttpServletRequest;
import ru.random.walk.authservice.model.dto.TokenExchangeRequest;
import ru.random.walk.authservice.model.dto.TokenRequest;
import ru.random.walk.authservice.model.dto.TokenResponse;

import java.util.Map;

public interface TokenFacade {
    TokenResponse postForToken(Map<String, Object> body, String clientId);
}
