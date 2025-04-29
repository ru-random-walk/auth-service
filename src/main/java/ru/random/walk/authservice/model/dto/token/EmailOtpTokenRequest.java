package ru.random.walk.authservice.model.dto.token;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(callSuper = true)
@Getter
public class EmailOtpTokenRequest extends TokenRequest {

    private String email;
    private String oneTimePassword;

    @Builder
    public EmailOtpTokenRequest(String clientId, String email, String oneTimePassword) {
        super(clientId);
        this.email = email;
        this.oneTimePassword = oneTimePassword;
    }
}
