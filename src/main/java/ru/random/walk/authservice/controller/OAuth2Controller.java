package ru.random.walk.authservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.random.walk.authservice.config.AuthServiceProperties;
import ru.random.walk.authservice.model.dto.JwkKeyDto;
import ru.random.walk.authservice.model.dto.JwkKeyResponse;
import ru.random.walk.authservice.model.dto.OAuthConfigurationResponse;
import ru.random.walk.authservice.service.JwtService;
import ru.random.walk.authservice.service.oauth2.providers.OAuth2RefreshTokenProvider;
import ru.random.walk.authservice.service.oauth2.providers.OAuth2TokenExchangeProvider;
import ru.random.walk.authservice.service.oauth2.providers.factory.OAuth2RefreshTokenRequestFactory;
import ru.random.walk.authservice.service.oauth2.providers.factory.OAuth2TokenExchangeRequestFactory;

import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class OAuth2Controller {

    private final AuthServiceProperties serviceProperties;
    private final JwtService jwtService;

    private final static List<String> SUPPORTED_GRANT_TYPES = List.of(
            OAuth2TokenExchangeRequestFactory.TOKEN_EXCHANGE_GRANT_TYPE,
            OAuth2RefreshTokenRequestFactory.REFRESH_TOKEN_GRANT_TYPE
    );

    private final static List<String> SUPPORTED_REQUEST_TYPES = List.of(
            "token"
    );

    @Operation(description = "Get authentication server metadata")
    @GetMapping("/.well-known/openid-configuration")
    public ResponseEntity<OAuthConfigurationResponse> getConfiguration() {
        String issuer = serviceProperties.getIssuerUrl();
        return ResponseEntity.ok(
                OAuthConfigurationResponse.builder()
                        .issuer(issuer)
                        .jwksUri(issuer + "/jwks")
                        .tokenEndpoint(issuer + "/token")
                        .supportedGrantTypes(SUPPORTED_GRANT_TYPES)
                        .supportedResponseType(SUPPORTED_REQUEST_TYPES)
                        .build()
        );
    }

    @Operation(description = "URL of the authorization server's JWK Set document")
    @GetMapping("/jwks")
    public ResponseEntity<JwkKeyResponse> getJwks() {
        RSAPublicKey publicKey = jwtService.getRsaPublicKey();
        String encodedModulus = Base64.getUrlEncoder().withoutPadding().encodeToString(publicKey.getModulus().toByteArray());
        String encodedExponent = Base64.getUrlEncoder().withoutPadding().encodeToString(publicKey.getPublicExponent().toByteArray());
        return ResponseEntity.ok(
            new JwkKeyResponse(List.of(
                    JwkKeyDto.builder()
                            .n(encodedModulus)
                            .e(encodedExponent)
                            .kid(JwtService.TOKEN_KEY_ID)
                            .kty("RSA")
                            .alg("RS256")
                            .build()
            ))
        );
    }
}
