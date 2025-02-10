package ru.random.walk.authservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.random.walk.authservice.model.entity.AuthUser;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Optional<AuthUser> findByEmail(String email);
    AuthUser createNewUser(AuthUser authUser);
    Page<AuthUser> getUsersPage(List<UUID> ids, Pageable pageable);
}
