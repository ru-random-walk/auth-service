package ru.random.walk.authservice.service.oauth2.exchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ru.random.walk.authservice.model.dto.clients.GoogleUserInfoDto;
import ru.random.walk.authservice.model.enam.AuthType;
import ru.random.walk.authservice.model.entity.AuthUser;
import ru.random.walk.authservice.model.exception.AuthBadRequestException;
import ru.random.walk.authservice.model.exception.AuthAuthorizationException;
import ru.random.walk.authservice.service.UserService;
import ru.random.walk.authservice.service.client.GoogleAuthClient;
import ru.random.walk.authservice.service.mapper.AuthUserMapper;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleAccessTokenExchanger implements AccessTokenExchanger {

    private final GoogleAuthClient googleAuthClient;
    private final UserService userService;
    private final AuthUserMapper authUserMapper;

    @Override
    public boolean supports(AuthType subjectProviderType) {
        return AuthType.GOOGLE.equals(subjectProviderType);
    }

    @Override
    public AuthUser exchange(String subjectToken) {
        var userInfoDto = tryToGetUserInfo(subjectToken);
        return userService.findByEmail(userInfoDto.email())
                .map(user -> useExistingUser(user, userInfoDto))
                .orElseGet(() -> createNewUser(userInfoDto));
    }

    private AuthUser useExistingUser(AuthUser user, GoogleUserInfoDto userInfoDto) {
        if (user.getAuthType() != AuthType.GOOGLE) {
            throw new AuthBadRequestException("User with this email already exists");
        }

        if (!Objects.equals(user.getAvatar(), userInfoDto.picture())) {
            user.setAvatar(userInfoDto.picture());
        }
        return user;
    }

    private AuthUser createNewUser(GoogleUserInfoDto userInfoDto) {
        var newUser = authUserMapper.fromGoogleDto(userInfoDto);
        return userService.createNewUser(newUser);
    }

    private GoogleUserInfoDto tryToGetUserInfo(String token) {
        try {
            return googleAuthClient.getUserInfo(token);
        } catch (WebClientResponseException exception) {
            log.error("Google threw exception", exception);

            if (exception.getStatusCode().value() == 401) {
                throw new AuthAuthorizationException("Google respond with UNAUTHORIZED error code");
            }

            throw exception;
        }
    }
}
