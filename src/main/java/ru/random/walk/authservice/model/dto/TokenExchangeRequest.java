package ru.random.walk.authservice.model.dto;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.random.walk.authservice.model.enam.AuthType;

@EqualsAndHashCode(callSuper = true)
@Getter
public class TokenExchangeRequest extends TokenRequest {
    private String subjectToken;
    private String subjectTokenType;
    private AuthType subjectProvider;

    @Builder
    public TokenExchangeRequest(String clientId, String subjectToken, String subjectTokenType, AuthType subjectProvider) {
        super(clientId);
        this.subjectToken = subjectToken;
        this.subjectTokenType = subjectTokenType;
        this.subjectProvider = subjectProvider;
    }

}
