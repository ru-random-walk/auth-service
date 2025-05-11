package ru.random.walk.authservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ru.random.walk.authservice.model.dto.ChangeUserInfoDto;
import ru.random.walk.authservice.model.dto.UserAvatarUrlDto;
import ru.random.walk.authservice.model.entity.AuthUser;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Optional<AuthUser> findByEmail(String email);
    AuthUser createNewUser(AuthUser authUser);
    Page<AuthUser> getUsersPage(List<UUID> ids, Pageable pageable);
    AuthUser findById(UUID id);
    AuthUser changeUser(UUID id, ChangeUserInfoDto changeDto);
    UserAvatarUrlDto uploadAvatar(UUID userId, MultipartFile file);
    UserAvatarUrlDto getAvatar(UUID userId);
    void removeAvatar(UUID userId);
}
