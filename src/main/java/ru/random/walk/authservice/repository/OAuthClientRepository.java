package ru.random.walk.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.random.walk.authservice.model.entity.OAuthClient;

@Repository
public interface OAuthClientRepository extends JpaRepository<OAuthClient, String> {
}
