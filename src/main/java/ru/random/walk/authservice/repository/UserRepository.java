package ru.random.walk.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.random.walk.authservice.model.entity.AuthUser;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<AuthUser, UUID> {

    Optional<AuthUser> findByEmail(String email);

}
