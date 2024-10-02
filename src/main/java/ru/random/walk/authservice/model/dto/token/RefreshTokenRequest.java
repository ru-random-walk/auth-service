package ru.random.walk.authservice.model.dto.token;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
@Getter
public class RefreshTokenRequest extends TokenRequest {

    private String refreshToken;

    @Builder
    public RefreshTokenRequest(String clientId, String refreshToken) {
        super(clientId);
        this.refreshToken = refreshToken;
    }
}
