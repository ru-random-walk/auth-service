package ru.random.walk.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.random.walk.authservice.model.enam.RoleName;
import ru.random.walk.authservice.model.entity.AuthUser;
import ru.random.walk.authservice.model.exception.AuthNotFoundException;
import ru.random.walk.authservice.repository.RoleRepository;
import ru.random.walk.authservice.repository.UserRepository;
import ru.random.walk.authservice.service.OutboxSenderService;
import ru.random.walk.authservice.service.UserService;
import ru.random.walk.authservice.service.mapper.AuthUserMapper;
import ru.random.walk.kafka.EventTopic;

import java.util.EnumSet;
import java.util.List;
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

    private void sendNewUserEvent(AuthUser newUser) {
        var eventDto = userMapper.toEventDto(newUser);
        outboxSenderService.sendMessageByOutbox(EventTopic.USER_REGISTRATION, eventDto);
    }
}
