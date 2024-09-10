package ru.random.walk.authservice.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "OAUTH_SCOPE")
public class OAuthScope {
    @Id
    private Integer id;
    @Column(name = "NAME")
    private String name;
}
