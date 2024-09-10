package ru.random.walk.authservice.service.oauth2.providers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.random.walk.authservice.model.dto.TokenExchangeRequest;
import ru.random.walk.authservice.model.dto.TokenRequest;
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

    public static final String TOKEN_EXCHANGE_GRANT_TYPE = "urn:ietf:params:oauth:grant-type:token-exchange";
    private final static String SUBJECT_TOKEN_KEY = "subject_token";
    private final static String SUBJECT_TOKEN_TYPE_KEY = "subject_token_type";
    private final static String SUBJECT_TOKEN_PROVIDER_KEY = "subject_token_provider";
    private static final String SUPPORTED_SUBJECT_TOKEN_TYPE = "Access Token";

    private final JwtService jwtService;
    private final List<AccessTokenExchanger> accessTokenExchangers;

    @Override
    public boolean supports(String grantType) {
        return TOKEN_EXCHANGE_GRANT_TYPE.equals(grantType);
    }

    @Override
    public TokenRequest generateRequest(String clientId, Map<String, Object> body) {
        if (!body.containsKey(SUBJECT_TOKEN_KEY)
                || !body.containsKey(SUBJECT_TOKEN_TYPE_KEY)
                || !body.containsKey(SUBJECT_TOKEN_PROVIDER_KEY)
        ) {
            throw new OAuth2BadRequestException("Invalid request");
        }

        String subjectToken = (String) body.get(SUBJECT_TOKEN_KEY);
        String subjectType = (String) body.get(SUBJECT_TOKEN_TYPE_KEY);
        String subjectProvider = (String) body.get(SUBJECT_TOKEN_PROVIDER_KEY);
        return TokenExchangeRequest.builder()
                .clientId(clientId)
                .subjectToken(subjectToken)
                .subjectProvider(AuthType.getByName(subjectProvider))
                .subjectTokenType(subjectType)
                .build();
    }

    @Override
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
