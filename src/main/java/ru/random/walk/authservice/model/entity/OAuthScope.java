package ru.random.walk.authservice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.random.walk.authservice.model.enam.ClientScope;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "OAUTH_SCOPE")
public class OAuthScope {
    @Id
    private Integer id;

    @Column(name = "NAME")
    @Enumerated(EnumType.STRING)
    private ClientScope name;
}
