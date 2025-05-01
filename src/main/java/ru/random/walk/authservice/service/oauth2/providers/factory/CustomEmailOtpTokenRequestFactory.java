package ru.random.walk.authservice.service.oauth2.providers.factory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.random.walk.authservice.model.dto.token.EmailOtpTokenRequest;
import ru.random.walk.authservice.model.dto.token.TokenRequest;
import ru.random.walk.authservice.model.exception.AuthBadRequestException;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomEmailOtpTokenRequestFactory implements OAuth2TokenRequestFactory {

    public static final String CUSTOM_EMAIL_OTP_GRANT_TYPE = "email_otp";

    private static final String EMAIL_KEY = "email";
    private static final String OTP_KEY = "otp";

    @Override
    public boolean supports(String grantType) {
        return CUSTOM_EMAIL_OTP_GRANT_TYPE.equals(grantType);
    }

    @Override
    public TokenRequest generateRequest(String clientId, Map<String, Object> body) {
        if (!body.containsKey(EMAIL_KEY) || !body.containsKey(OTP_KEY)) {
            throw new AuthBadRequestException(String.format("This grant type should contain %s and %s fields", EMAIL_KEY, OTP_KEY));
        }
        String email = (String) body.get(EMAIL_KEY);
        String otp = (String) body.get(OTP_KEY);

        return EmailOtpTokenRequest.builder()
                .oneTimePassword(otp)
                .email(email)
                .clientId(clientId)
                .build();
    }
}
