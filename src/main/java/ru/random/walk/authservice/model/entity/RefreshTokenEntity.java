package ru.random.walk.authservice.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "REFRESH_TOKEN")
public class RefreshTokenEntity {
    @Id
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "token")
    private UUID token;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
}
