package ru.random.walk.authservice.service.oauth2.exchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ru.random.walk.authservice.model.dto.clients.YandexUserInfoDto;
import ru.random.walk.authservice.model.enam.AuthType;
import ru.random.walk.authservice.model.entity.AuthUser;
import ru.random.walk.authservice.model.exception.AuthAuthorizationException;
import ru.random.walk.authservice.model.exception.AuthBadRequestException;
import ru.random.walk.authservice.service.UserService;
import ru.random.walk.authservice.service.client.YandexAuthClient;
import ru.random.walk.authservice.service.mapper.AuthUserMapper;

import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class YandexAccessTokenExchanger implements AccessTokenExchanger {

    private final UserService userService;
    private final YandexAuthClient yandexAuthClient;
    private final AuthUserMapper authUserMapper;

    private static final String AUTH_HEADER_PREFIX = "OAuth ";
    private static final String AVATAR_URL_FORMAT = "https://avatars.yandex.net/get-yapic/%s/islands-retina-50";

    @Override
    public boolean supports(AuthType subjectProviderType) {
        return AuthType.YANDEX == subjectProviderType;
    }

    @Override
    public AuthUser exchange(String subjectToken) {
        var userInfoDto = tryToGetUserInfo(subjectToken);
        return userService.findByEmail(userInfoDto.email())
                .map(user -> useExistingUser(user, userInfoDto))
                .orElseGet(() -> createNewUser(userInfoDto));
    }

    private YandexUserInfoDto tryToGetUserInfo(String token) {
        try {
            return yandexAuthClient.getUserInfo(AUTH_HEADER_PREFIX + token, "json");
        } catch (WebClientResponseException exception) {
            log.error("Yandex threw exception", exception);

            if (exception.getStatusCode().value() == 401) {
                throw new AuthAuthorizationException("Yandex respond with UNAUTHORIZED error code");
            }

            throw exception;
        }
    }

    private AuthUser useExistingUser(AuthUser user, YandexUserInfoDto userInfoDto) {
        if (user.getAuthType() != AuthType.YANDEX) {
            throw new AuthBadRequestException("User with this email already exists");
        }
        refreshAvatar(user, userInfoDto);

        return user;
    }

    private void refreshAvatar(AuthUser user, YandexUserInfoDto userInfoDto) {
        if (Objects.nonNull(userInfoDto.isAvatarEmpty()) && !userInfoDto.isAvatarEmpty()) {
            String avatarUrl = getAvatarUrl(userInfoDto.avatarId());
            if (!Objects.equals(user.getAvatar(), avatarUrl)) {
                user.setAvatar(avatarUrl);
            }
        }
    }

    private AuthUser createNewUser(YandexUserInfoDto userInfoDto) {
        var newUser = authUserMapper.fromYandexDto(userInfoDto, getAvatarUrl(userInfoDto.avatarId()));
        return userService.createNewUser(newUser);
    }

    private String getAvatarUrl(String avatarId) {
        return String.format(AVATAR_URL_FORMAT, avatarId);
    }
}
