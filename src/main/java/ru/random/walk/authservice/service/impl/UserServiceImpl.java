package ru.random.walk.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.random.walk.authservice.model.dto.ChangeUserInfoDto;
import ru.random.walk.authservice.model.dto.UserAvatarUrlDto;
import ru.random.walk.authservice.model.enam.RoleName;
import ru.random.walk.authservice.model.entity.AuthUser;
import ru.random.walk.authservice.model.exception.AuthNotFoundException;
import ru.random.walk.authservice.repository.RoleRepository;
import ru.random.walk.authservice.repository.UserRepository;
import ru.random.walk.authservice.service.OutboxSenderService;
import ru.random.walk.authservice.service.UserService;
import ru.random.walk.authservice.service.mapper.AuthUserMapper;
import ru.random.walk.client.StorageClient;
import ru.random.walk.config.StorageProperties;
import ru.random.walk.model.FileType;
import ru.random.walk.topic.EventTopic;
import ru.random.walk.util.PathBuilder;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static ru.random.walk.authservice.model.enam.RoleName.DEFAULT_USER;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OutboxSenderService outboxSenderService;
    private final AuthUserMapper userMapper;
    private final StorageClient storageClient;
    private final StorageProperties storageProperties;
    private static final String AVATAR_OBJECT_PREFIX = "avatar/";
    private static final EnumSet<RoleName> DEFAULT_USER_ROLES = EnumSet.of(DEFAULT_USER);

    @Override
    public Optional<AuthUser> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public AuthUser createNewUser(AuthUser authUser) {
        var defaultRoles = roleRepository.findAllByNameIn(DEFAULT_USER_ROLES);
        authUser.getRoles().addAll(defaultRoles);

        var newUser = userRepository.save(authUser);
        sendNewUserEvent(newUser);

        return newUser;
    }

    @Override
    public Page<AuthUser> getUsersPage(List<UUID> ids, Pageable pageable) {
        return userRepository.findAllByIdIn(ids, pageable);
    }

    @Override
    public AuthUser findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AuthNotFoundException("User with id " + id + " does not exist"));
    }

    @Override
    public AuthUser changeUser(UUID id, ChangeUserInfoDto changeDto) {
        AuthUser user = findById(id);
        user.setFullName(changeDto.fullName());
        user.setDescription(changeDto.aboutMe());
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public UserAvatarUrlDto uploadAvatar(UUID userId, MultipartFile file) {
        log.info("Uploading new avatar for user {}", userId);
        AuthUser user = findById(userId);
        user.setExternalAvatarUrl(null);
        user.incrementAvatarVersion();

        String imageTemporaryUrl = uploadAvatarImage(file, user.getId());
        LocalDateTime expiresAt = getAvatarUrlExpirationTime();

        userRepository.save(user);
        log.info("Avatar has been successfully uploaded for {}", userId);
        return userMapper.toAvatarUrlDto(user, imageTemporaryUrl, expiresAt);
    }

    @Override
    public UserAvatarUrlDto getAvatar(UUID userId) {
        log.debug("Getting avatar for user {}", userId);
        AuthUser user = findById(userId);
        if (Objects.equals(user.getAvatarVersion(), 0L)) {
            return userMapper.toAvatarUrlDto(user, user.getExternalAvatarUrl(), null);
        } else {
            String url = storageClient.getUrl(getAvatarStorageKey(userId));
            return userMapper.toAvatarUrlDto(user, url, getAvatarUrlExpirationTime());
        }
    }

    @Transactional
    @Override
    public void removeAvatar(UUID userId) {
        log.info("Removing avatar for user {}", userId);
        AuthUser user = findById(userId);
        user.setDefaultAvatarVersion();
        user.setExternalAvatarUrl(null);

        String storageKey = getAvatarStorageKey(userId);
        if (storageClient.exist(storageKey)) {
            storageClient.delete(storageKey);
        }

        userRepository.save(user);
        log.info("Avatar was removed for user {}", userId);
    }

    private void sendNewUserEvent(AuthUser newUser) {
        var eventDto = userMapper.toEventDto(newUser);
        outboxSenderService.sendMessageByOutbox(EventTopic.USER_REGISTRATION, eventDto);
    }

    private String uploadAvatarImage(MultipartFile file, UUID userId) {
        try (var photoIO = file.getInputStream()) {
            return storageClient.uploadAndGetUrl(photoIO, getAvatarStorageKey(userId), FileType.PNG);
        } catch (Exception e) {
            throw new RuntimeException("Error uploading user's avatar", e);
        }
    }

    private String getAvatarStorageKey(UUID userId) {
        return PathBuilder.init()
                .add("user-avatar")
                .add(PathBuilder.Key.USER_ID, userId)
                .build();
    }

    private LocalDateTime getAvatarUrlExpirationTime() {
        return LocalDateTime.now().plusMinutes(storageProperties.temporaryUrlTtlInMinutes());
    }
}
