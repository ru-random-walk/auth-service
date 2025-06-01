package ru.random.walk.authservice.service.facade.impl;

import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.random.walk.authservice.model.dto.ChangeUserInfoDto;
import ru.random.walk.authservice.model.dto.DetailedUserDto;
import ru.random.walk.authservice.model.dto.UserAvatarUrlDto;
import ru.random.walk.authservice.model.dto.UserDto;
import ru.random.walk.authservice.model.exception.AuthBadRequestException;
import ru.random.walk.authservice.service.RefreshTokenService;
import ru.random.walk.authservice.service.UserService;
import ru.random.walk.authservice.service.facade.UserFacade;
import ru.random.walk.authservice.service.mapper.AuthUserMapper;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade {

    private final UserService userService;
    private final AuthUserMapper userMapper;
    private final RefreshTokenService refreshTokenService;

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif");

    @Override
    public Page<UserDto> getUsers(List<UUID> ids, Pageable pageable) {
        return userService.getUsersPage(ids, pageable)
                .map(userMapper::toUserDto);
    }

    @Override
    public DetailedUserDto getSelfInfo(String name) {
        return userMapper.toDetailedUserDto(userService.findById(UUID.fromString(name)));
    }

    @Override
    public DetailedUserDto changeUserInfo(String name, ChangeUserInfoDto dto) {
        if (Strings.isNullOrEmpty(dto.fullName())) {
            throw new AuthBadRequestException("Full name cannot be empty");
        }
        UUID id = UUID.fromString(name);
        return userMapper.toDetailedUserDto(userService.changeUser(id, dto));
    }

    @Override
    public void logoutUser(String name) {
        UUID userId = UUID.fromString(name);
        refreshTokenService.removeTokenForUser(userId);
    }

    @Transactional
    @Override
    public UserAvatarUrlDto uploadAvatar(UUID userId, MultipartFile file) {
        checkFileExtension(file);
        return userService.uploadAvatar(userId, file);
    }

    @Override
    public UserAvatarUrlDto getAvatarForUser(UUID userId) {
        return userService.getAvatar(userId);
    }

    @Override
    public void removeAvatar(UUID userId) {
        userService.removeAvatar(userId);
    }

    private void checkFileExtension(MultipartFile file) {
        if (file == null || file.isEmpty() || Strings.isNullOrEmpty(file.getOriginalFilename())) {
            throw new AuthBadRequestException("File was expected");
        }
        String fileName = file.getOriginalFilename();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(fileExtension)) {
            throw new AuthBadRequestException("Unsupported file format");
        }
    }
}
