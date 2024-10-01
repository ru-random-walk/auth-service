package ru.random.walk.authservice.service.oauth2.providers.factory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.random.walk.authservice.model.dto.token.TokenExchangeRequest;
import ru.random.walk.authservice.model.dto.token.TokenRequest;
import ru.random.walk.authservice.model.enam.AuthType;
import ru.random.walk.authservice.model.exception.OAuth2BadRequestException;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class OAuth2TokenExchangeRequestFactory implements OAuth2TokenRequestFactory{

    public static final String TOKEN_EXCHANGE_GRANT_TYPE = "urn:ietf:params:oauth:grant-type:token-exchange";
    private static final String SUBJECT_TOKEN_KEY = "subject_token";
    private static final String SUBJECT_TOKEN_TYPE_KEY = "subject_token_type";
    private static final String SUBJECT_TOKEN_PROVIDER_KEY = "subject_token_provider";

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
}
