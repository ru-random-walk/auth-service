package ru.random.walk.authservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.random.walk.authservice.model.enam.RoleName;
import ru.random.walk.authservice.model.entity.AuthUser;
import ru.random.walk.authservice.repository.RoleRepository;
import ru.random.walk.authservice.repository.UserRepository;
import ru.random.walk.authservice.service.UserService;

import java.util.EnumSet;
import java.util.Optional;

import static ru.random.walk.authservice.model.enam.RoleName.DEFAULT_USER;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private static final EnumSet<RoleName> DEFAULT_USER_ROLES = EnumSet.of(DEFAULT_USER);

    @Override
    public Optional<AuthUser> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public AuthUser createNewUser(AuthUser authUser) {
        var defaultRoles = roleRepository.findAllByNameIn(DEFAULT_USER_ROLES);
        authUser.getRoles().addAll(defaultRoles);

        return userRepository.save(authUser);
    }
}
