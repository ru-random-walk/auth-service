package ru.random.walk.authservice.repository;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import ru.random.walk.authservice.model.entity.OutboxMessage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OutboxRepository extends JpaRepository<OutboxMessage, UUID> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    List<OutboxMessage> findAllBySentFalse();

    List<OutboxMessage> getAllByCreatedAtBefore(LocalDateTime createdUntilDate);
}
