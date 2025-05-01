package ru.random.walk.authservice.service.oauth2.providers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.random.walk.authservice.model.dto.TokenResponse;
import ru.random.walk.authservice.model.dto.token.EmailOtpTokenRequest;
import ru.random.walk.authservice.model.dto.token.TokenRequest;
import ru.random.walk.authservice.model.enam.AuthType;
import ru.random.walk.authservice.model.entity.AuthUser;
import ru.random.walk.authservice.model.exception.AuthAuthorizationException;
import ru.random.walk.authservice.service.JwtService;
import ru.random.walk.authservice.service.OneTimePasswordService;
import ru.random.walk.authservice.service.UserService;
import ru.random.walk.authservice.service.mapper.AuthUserMapper;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomEmailOtpTokenProvider implements OAuth2TokenProvider {

    private final OneTimePasswordService oneTimePasswordService;
    private final JwtService jwtService;
    private final AuthUserMapper userMapper;
    private final UserService userService;

    @Override
    public boolean supports(Class<? extends TokenRequest> clazz) {
        return EmailOtpTokenRequest.class == clazz;
    }

    @Transactional
    @Override
    public TokenResponse handle(TokenRequest tokenRequest) {
        EmailOtpTokenRequest request = (EmailOtpTokenRequest) tokenRequest;
        String email = request.getEmail();
        String password = request.getOneTimePassword();
        if (!oneTimePasswordService.isValidPassword(email, password)) {
            throw new AuthAuthorizationException("Access denied");
        }

        AuthUser authUser = userService.findByEmail(email)
                .filter(user -> user.getAuthType() == AuthType.PASSWORD)
                .orElseGet(() -> createUser(email));
        oneTimePasswordService.deletePasswordByEmail(email);

        return jwtService.generateToken(tokenRequest, authUser);
    }

    private AuthUser createUser(String email) {
        AuthUser user = userMapper.createCustomUser(email, AuthType.PASSWORD);
        return userService.createNewUser(user);
    }

}
