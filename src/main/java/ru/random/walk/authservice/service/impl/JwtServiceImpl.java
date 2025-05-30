package ru.random.walk.authservice.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.random.walk.authservice.config.AuthServiceProperties;
import ru.random.walk.authservice.config.security.jwt.JwtProperties;
import ru.random.walk.authservice.model.dto.token.ClientCredentialsTokenRequest;
import ru.random.walk.authservice.model.dto.token.TokenRequest;
import ru.random.walk.authservice.model.dto.TokenResponse;
import ru.random.walk.authservice.model.enam.ClientScope;
import ru.random.walk.authservice.model.enam.RoleName;
import ru.random.walk.authservice.model.entity.AuthUser;
import ru.random.walk.authservice.model.entity.Role;
import ru.random.walk.authservice.service.JwtService;
import ru.random.walk.authservice.service.RefreshTokenService;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final JwtProperties jwtProperties;
    private final AuthServiceProperties authServiceProperties;
    private final RefreshTokenService refreshTokenService;

    @Override
    public TokenResponse generateToken(TokenRequest tokenRequest, AuthUser user) {
        var accessToken = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("kid", TOKEN_KEY_ID)
                .setIssuer(authServiceProperties.getIssuerUrl())
                .signWith(getPrivateKey(), SignatureAlgorithm.RS256)
                .claim("client_id", tokenRequest.getClientId())
                .claim("authorities", getAuthorities(tokenRequest, user))
                .setExpiration(
                        Date.from(
                                LocalDateTime.now()
                                        .plusSeconds(jwtProperties.getExpireTimeInSeconds())
                                        .atZone(ZoneId.systemDefault())
                                        .toInstant()
                        )
                )
                .setSubject(getSubject(tokenRequest, user))
                .compact();

        return TokenResponse.builder()
                .tokenType("Bearer")
                .accessToken(accessToken)
                .refreshToken(getRefreshToken(user))
                .expiresIn(jwtProperties.getExpireTimeInSeconds())
                .build();
    }

    private List<String> getAuthorities(TokenRequest tokenRequest, AuthUser user) {
        if (tokenRequest instanceof ClientCredentialsTokenRequest clientCredentialsRequest) {
            return clientCredentialsRequest.getScopes().stream()
                    .map(ClientScope::name)
                    .toList();
        } else {
            return user.getRoles().stream()
                    .map(Role::getName)
                    .map(RoleName::name)
                    .toList();
        }
    }

    private String getSubject(TokenRequest tokenRequest, AuthUser user) {
        if (tokenRequest instanceof ClientCredentialsTokenRequest clientCredentialsRequest) {
            return clientCredentialsRequest.getClientId();
        } else {
            return user.getId().toString();
        }
    }

    @Nullable
    private String getRefreshToken(AuthUser user) {
        if (user == null) {
            return null;
        }
        var refreshToken = refreshTokenService.refreshTokenForUser(user);
        return refreshToken.getToken().toString();
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
            log.error("Error decrypting public key", e);
            return null;
        }
    }
}
