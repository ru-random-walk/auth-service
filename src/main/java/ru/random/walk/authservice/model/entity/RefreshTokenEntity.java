package ru.random.walk.authservice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
    @Column(name = "token")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID token;

    @OneToOne
    @JoinColumn(name = "user_id")
    private AuthUser user;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
}
