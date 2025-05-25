package ru.random.walk.authservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.random.walk.authservice.model.dto.ChangeUserInfoDto;
import ru.random.walk.authservice.model.dto.DetailedUserDto;
import ru.random.walk.authservice.model.dto.UserAvatarUrlDto;
import ru.random.walk.authservice.model.dto.UserDto;
import ru.random.walk.authservice.model.exception.AuthTooManyRequestsException;
import ru.random.walk.authservice.service.facade.UserFacade;
import ru.random.walk.util.KeyRateLimiter;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserFacade userFacade;
    private final KeyRateLimiter<String> getUserAvatarRateLimiter;
    private final KeyRateLimiter<UUID> uploadUserAvatarRateLimiter;

    @Operation(summary = "Get users info by their ids")
    @GetMapping("/users")
    public Page<UserDto> getUsers(
            @RequestParam("ids") List<UUID> ids,
            @ParameterObject Pageable pageable
    ) {
        return userFacade.getUsers(ids, pageable);
    }

    @Operation(summary = "Get info about current user")
    @GetMapping("/userinfo/me")
    public DetailedUserDto getSelfInfo(Principal principal) {
        return userFacade.getSelfInfo(principal.getName());
    }

    @Operation(summary = "Change current user's information")
    @PutMapping("/userinfo/change")
    public DetailedUserDto changeSelfInfo(@RequestBody ChangeUserInfoDto dto, Principal principal) {
        log.info("Changing information about user {}, dto: {}", principal.getName(), dto);
        return userFacade.changeUserInfo(principal.getName(), dto);
    }

    @Operation(summary = "Logout and remove user's refresh token")
    @PostMapping("/userinfo/logout")
    public void logOut(Principal principal) {
        log.info("Logging out user {}", principal.getName());
        userFacade.logoutUser(principal.getName());
    }

    @PutMapping("/userinfo/avatar/upload")
    public UserAvatarUrlDto uploadAvatar(@RequestPart("file") MultipartFile file, Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        uploadUserAvatarRateLimiter.throwIfRateLimitExceeded(
                userId,
                () -> new AuthTooManyRequestsException("Limit exceeded for PUT /userinfo/avatar/upload")
        );
        return userFacade.uploadAvatar(userId, file);
    }

    @GetMapping("/userinfo/{userId}/avatar")
    public UserAvatarUrlDto getAvatar(@PathVariable("userId") UUID userId, Principal principal) {
        UUID requesterId = UUID.fromString(principal.getName());
        getUserAvatarRateLimiter.throwIfRateLimitExceeded(
                String.format("requester-%s:owner-%s", requesterId, userId),
                () -> new AuthTooManyRequestsException(String.format("Limit exceeded for user %s for GET /userinfo/%s/avatar", requesterId, userId))
        );
        return userFacade.getAvatarForUser(userId);
    }

    @DeleteMapping("/userinfo/avatar/remove")
    public void removeAvatar(Principal principal) {
        UUID userId = UUID.fromString(principal.getName());
        userFacade.removeAvatar(userId);
    }
}
