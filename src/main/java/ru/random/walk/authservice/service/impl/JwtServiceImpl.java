package ru.random.walk.authservice.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.random.walk.authservice.config.AuthServiceProperties;
import ru.random.walk.authservice.config.security.jwt.JwtProperties;
import ru.random.walk.authservice.model.dto.TokenRequest;
import ru.random.walk.authservice.model.dto.TokenResponse;
import ru.random.walk.authservice.model.entity.AuthUser;
import ru.random.walk.authservice.model.entity.Role;
import ru.random.walk.authservice.service.JwtService;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtProperties jwtProperties;
    private final AuthServiceProperties authServiceProperties;

    @Override
    public TokenResponse generateToken(TokenRequest tokenRequest, AuthUser user) {
        var token = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("kid", TOKEN_KEY_ID)
                .setIssuer(authServiceProperties.getIssuerUrl())
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256)
                .claim("client_id", tokenRequest.getClientId())
                .claim("authorities", user.getRoles().stream().map(Role::getName).toList())
                .setExpiration(Date.from(LocalDateTime.now().plusMinutes(jwtProperties.getExpireTimeInMinutes()).atZone(ZoneId.systemDefault()).toInstant()))
                .setSubject(user.getId().toString())
                .compact();

        return TokenResponse.builder()
                .tokenType("Bearer")
                .accessToken(token)
                .expiresIn(jwtProperties.getExpireTimeInMinutes())
                .build();
    }

    private PrivateKey getPrivateKey() {
        try {
            var cleanKey = jwtProperties.getPrivateKey().replaceAll("\\s+", "");
            var keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(cleanKey));
            var keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            log.error("Error creating JWT token", e);
            return null;
        }
    }

    @Override
    public RSAPublicKey getRsaPublicKey() {
        try {
            var cleanKey = jwtProperties.getPublicKey().replaceAll("\\s+", "");
            var keyFactory = KeyFactory.getInstance("RSA");
            var keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(cleanKey));
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            log.error("Error decrypting public key");
            return null;
        }
    }
}
