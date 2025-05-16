package ru.random.walk.authservice.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.random.walk.authservice.model.dto.DetailedUserDto;
import ru.random.walk.authservice.model.dto.UserAvatarUrlDto;
import ru.random.walk.authservice.model.dto.clients.GoogleUserInfoDto;
import ru.random.walk.authservice.model.dto.UserDto;
import ru.random.walk.authservice.model.dto.clients.YandexUserInfoDto;
import ru.random.walk.authservice.model.enam.AuthType;
import ru.random.walk.authservice.model.entity.AuthUser;
import ru.random.walk.dto.RegisteredUserInfoEvent;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface AuthUserMapper {
    @Mapping(source = "userInfoDto.givenName", target = "fullName")
    @Mapping(source = "userInfoDto.email", target = "email")
    @Mapping(target = "username", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "authType", expression = "java(ru.random.walk.authservice.model.enam.AuthType.GOOGLE)")
    @Mapping(target = "externalAvatarUrl", source = "picture")
    AuthUser fromGoogleDto(GoogleUserInfoDto userInfoDto);

    @Mapping(target = "fullName", source = "userInfoDto", qualifiedByName = "getFullNameFromYandex")
    @Mapping(target = "email", source = "userInfoDto.email")
    @Mapping(target = "username", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "authType", expression = "java(ru.random.walk.authservice.model.enam.AuthType.YANDEX)")
    @Mapping(target = "externalAvatarUrl", source = "avatarUrl")
    AuthUser fromYandexDto(YandexUserInfoDto userInfoDto, String avatarUrl);

    RegisteredUserInfoEvent toEventDto(AuthUser user);

    @Mapping(source = "externalAvatarUrl", target = "avatar")
    UserDto toUserDto(AuthUser user);

    @Mapping(source = "externalAvatarUrl", target = "avatar")
    DetailedUserDto toDetailedUserDto(AuthUser user);

    default AuthUser createCustomUser(String email, AuthType authType) {
        var user = new AuthUser();
        user.setEmail(email);
        user.setAuthType(authType);
        user.setUsername(email);
        user.setFullName("User" + UUID.randomUUID());
        return user;
    }

    default UserAvatarUrlDto toAvatarUrlDto(AuthUser user, String avatarUrl, LocalDateTime expiresAt) {
        return new UserAvatarUrlDto(user.getId(), user.getAvatarVersion(), avatarUrl, expiresAt);
    }

    @Named("getFullNameFromYandex")
    default String getFullNameFromYandex(YandexUserInfoDto userInfoDto) {
        return String.format("%s %s", userInfoDto.firstName(), userInfoDto.lastName()).trim();
    }
}
