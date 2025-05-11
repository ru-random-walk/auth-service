package ru.random.walk.authservice.service.facade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ru.random.walk.authservice.model.dto.ChangeUserInfoDto;
import ru.random.walk.authservice.model.dto.DetailedUserDto;
import ru.random.walk.authservice.model.dto.UserAvatarUrlDto;
import ru.random.walk.authservice.model.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface UserFacade {
    Page<UserDto> getUsers(List<UUID> ids, Pageable pageable);

    DetailedUserDto getSelfInfo(String name);

    DetailedUserDto changeUserInfo(String name, ChangeUserInfoDto dto);

    UserAvatarUrlDto uploadAvatar(UUID userId, MultipartFile file);

    UserAvatarUrlDto getAvatarForUser(UUID userId);

    void removeAvatar(UUID userId);

    void logoutUser(String name);
}
