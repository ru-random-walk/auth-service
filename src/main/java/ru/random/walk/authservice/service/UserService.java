package ru.random.walk.authservice.service;

import ru.random.walk.authservice.model.entity.AuthUser;

import java.util.Optional;

public interface UserService {
    Optional<AuthUser> findByEmail(String email);
    AuthUser createNewUser(AuthUser authUser);
}
