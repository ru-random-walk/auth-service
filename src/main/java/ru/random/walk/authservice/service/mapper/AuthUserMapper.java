package ru.random.walk.authservice.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.random.walk.authservice.model.dto.GoogleUserInfoDto;
import ru.random.walk.authservice.model.entity.AuthUser;

@Mapper(componentModel = "spring")
public interface AuthUserMapper {
    @Mapping(source = "userInfoDto.givenName", target = "fullName")
    @Mapping(source = "userInfoDto.email", target = "email")
    @Mapping(target = "username", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "authType", expression = "java(ru.random.walk.authservice.model.enam.AuthType.GOOGLE)")
    AuthUser fromGoogleDto(GoogleUserInfoDto userInfoDto);
}
