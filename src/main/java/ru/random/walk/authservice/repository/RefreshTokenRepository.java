package ru.random.walk.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.random.walk.authservice.model.entity.AuthUser;
import ru.random.walk.authservice.model.entity.RefreshTokenEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, UUID> {

    Optional<RefreshTokenEntity> findByToken(UUID token);

}
