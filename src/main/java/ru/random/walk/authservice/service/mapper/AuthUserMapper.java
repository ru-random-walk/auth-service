package ru.random.walk.authservice.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.random.walk.authservice.model.dto.DetailedUserDto;
import ru.random.walk.authservice.model.dto.GoogleUserInfoDto;
import ru.random.walk.authservice.model.dto.UserDto;
import ru.random.walk.authservice.model.enam.AuthType;
import ru.random.walk.authservice.model.entity.AuthUser;
import ru.random.walk.dto.RegisteredUserInfoEvent;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface AuthUserMapper {
    @Mapping(source = "userInfoDto.givenName", target = "fullName")
    @Mapping(source = "userInfoDto.email", target = "email")
    @Mapping(target = "username", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "authType", expression = "java(ru.random.walk.authservice.model.enam.AuthType.GOOGLE)")
    @Mapping(target = "avatar", source = "picture")
    AuthUser fromGoogleDto(GoogleUserInfoDto userInfoDto);

    RegisteredUserInfoEvent toEventDto(AuthUser user);

    UserDto toUserDto(AuthUser user);

    DetailedUserDto toDetailedUserDto(AuthUser user);

    default AuthUser createCustomUser(String email, AuthType authType) {
        var user = new AuthUser();
        user.setEmail(email);
        user.setAuthType(authType);
        user.setUsername(email);
        user.setFullName("User" + UUID.randomUUID());
        return user;
    }
}
