package ru.random.walk.authservice.service.oauth2.exchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ru.random.walk.authservice.model.dto.GoogleUserInfoDto;
import ru.random.walk.authservice.model.enam.AuthType;
import ru.random.walk.authservice.model.entity.AuthUser;
import ru.random.walk.authservice.model.exception.OAuth2BadRequestException;
import ru.random.walk.authservice.model.exception.OAuth2AuthorizationException;
import ru.random.walk.authservice.service.UserService;
import ru.random.walk.authservice.service.client.GoogleAuthClient;
import ru.random.walk.authservice.service.mapper.AuthUserMapper;

import java.util.Optional;

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

        Optional<AuthUser> user = userService.findByEmail(userInfoDto.email());
        if (user.isPresent()) {

            if (user.get().getAuthType() != AuthType.GOOGLE) {
                throw new OAuth2BadRequestException("User with this email already exists");
            }
            return user.get();

        } else {
            var newUser = authUserMapper.fromGoogleDto(userInfoDto);
            return userService.createNewUser(newUser);
        }
    }

    private GoogleUserInfoDto tryToGetUserInfo(String token) {
        try {
            return googleAuthClient.getUserInfo(token);
        } catch (WebClientResponseException exception) {
            log.error("Google threw exception", exception);

            if (exception.getStatusCode().value() == 401) {
                throw new OAuth2AuthorizationException("Google respond with UNAUTHORIZED error code");
            }

            throw exception;
        }
    }
}
