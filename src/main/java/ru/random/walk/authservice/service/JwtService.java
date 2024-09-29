package ru.random.walk.authservice.service;

import ru.random.walk.authservice.model.dto.token.TokenRequest;
import ru.random.walk.authservice.model.dto.TokenResponse;
import ru.random.walk.authservice.model.entity.AuthUser;

import java.security.interfaces.RSAPublicKey;

public interface JwtService {

    String TOKEN_KEY_ID = "rw_key";

    TokenResponse generateToken(TokenRequest tokenRequest, AuthUser user);

    RSAPublicKey getRsaPublicKey();
}
