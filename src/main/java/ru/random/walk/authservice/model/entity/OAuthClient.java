package ru.random.walk.authservice.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "OAUTH_CLIENT")
public class OAuthClient {

    @Id
    @Column(name = "CLIENT_ID")
    private String clientId;

    @Column(name = "CLIENT_SECRET")
    private String clientSecret;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "CLIENT_SCOPE",
            joinColumns = {@JoinColumn(name = "CLIENT_ID")},
            inverseJoinColumns = {@JoinColumn(name = "SCOPE_ID")}
    )
    private List<OAuthScope> scopes;

    public List<GrantedAuthority> getAuthorities() {
        return scopes.stream()
                .map(scope -> new SimpleGrantedAuthority(scope.getName().name()))
                .collect(Collectors.toList());
    }
}
