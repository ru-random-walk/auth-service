package ru.random.walk.authservice.service.oauth2.providers;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.random.walk.authservice.model.dto.token.TokenExchangeRequest;
import ru.random.walk.authservice.model.dto.token.TokenRequest;
import ru.random.walk.authservice.model.dto.TokenResponse;
import ru.random.walk.authservice.model.enam.AuthType;
import ru.random.walk.authservice.model.entity.AuthUser;
import ru.random.walk.authservice.model.exception.OAuth2BadRequestException;
import ru.random.walk.authservice.service.JwtService;
import ru.random.walk.authservice.service.oauth2.exchange.AccessTokenExchanger;

import java.util.List;
import java.util.Map;


@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth2TokenExchangeProvider implements OAuth2TokenProvider {

    private static final String SUPPORTED_SUBJECT_TOKEN_TYPE = "Access Token";

    private final JwtService jwtService;
    private final List<AccessTokenExchanger> accessTokenExchangers;

    @Override
    public boolean supports(Class<? extends TokenRequest> clazz) {
        return clazz == TokenExchangeRequest.class;
    }

    @Override
    @Transactional
    public TokenResponse handle(TokenRequest tokenRequest) {
        var request = (TokenExchangeRequest) tokenRequest;
        if (!SUPPORTED_SUBJECT_TOKEN_TYPE.equals(request.getSubjectTokenType())) {
            throw new OAuth2BadRequestException("Tokens with type: " + request.getSubjectTokenType() + " is not supported");
        }

        AuthUser user = accessTokenExchangers.stream()
                .filter(exchanger -> exchanger.supports(request.getSubjectProvider()))
                .map(exchanger -> exchanger.exchange(request.getSubjectToken()))
                .findFirst()
                .orElseThrow(() -> new OAuth2BadRequestException("Unsupported token provider"));

         return jwtService.generateToken(tokenRequest, user);
    }

}
