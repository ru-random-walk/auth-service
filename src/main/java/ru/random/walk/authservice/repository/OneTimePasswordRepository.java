package ru.random.walk.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.random.walk.authservice.model.entity.OneTimePassword;

import java.time.LocalDateTime;

public interface OneTimePasswordRepository extends JpaRepository<OneTimePassword, String> {

    @Modifying
    @Query("delete from OneTimePassword otp where otp.expiresAt < :beforeDate")
    int deleteAllByExpiresAtBefore(LocalDateTime beforeDate);
}
