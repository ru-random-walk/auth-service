package ru.random.walk.authservice.service.oauth2.exchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ru.random.walk.authservice.model.dto.clients.VkUserInfoDto;
import ru.random.walk.authservice.model.enam.AuthType;
import ru.random.walk.authservice.model.entity.AuthUser;
import ru.random.walk.authservice.model.exception.AuthAuthorizationException;
import ru.random.walk.authservice.model.exception.AuthBadRequestException;
import ru.random.walk.authservice.service.UserService;
import ru.random.walk.authservice.service.client.VkAuthClient;
import ru.random.walk.authservice.service.mapper.AuthUserMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class VkAccessTokenExchanger implements AccessTokenExchanger{

    private final VkAuthClient vkAuthClient;
    private final UserService userService;
    private final AuthUserMapper authUserMapper;

    @Value("${auth.vk.client-id}")
    private String vkClientId;

    @Override
    public boolean supports(AuthType subjectProviderType) {
        return subjectProviderType == AuthType.VK;
    }

    @Override
    public AuthUser exchange(String subjectToken) {
        var userInfoDto = tryToGetUserInfo(subjectToken);
        return userService.findByEmail(userInfoDto.user().email())
                .map(this::useExistingUser)
                .orElseGet(() -> createNewUser(userInfoDto));
    }

    private VkUserInfoDto tryToGetUserInfo(String token) {
        try {
            return vkAuthClient.getUserInfo(vkClientId, token);
        } catch (WebClientResponseException exception) {
            log.error("VK threw exception", exception);

            if (exception.getStatusCode().value() == 401) {
                throw new AuthAuthorizationException("VK respond with UNAUTHORIZED error code");
            }

            throw exception;
        }
    }

    private AuthUser useExistingUser(AuthUser user) {
        if (user.getAuthType() != AuthType.VK) {
            throw new AuthBadRequestException("User with this email already exists");
        }

        return user;
    }

    private AuthUser createNewUser(VkUserInfoDto userInfoDto) {
        var newUser = authUserMapper.fromVkDto(userInfoDto);
        return userService.createNewUser(newUser);
    }
}
