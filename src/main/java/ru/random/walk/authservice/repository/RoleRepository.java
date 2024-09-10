package ru.random.walk.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.random.walk.authservice.model.enam.RoleName;
import ru.random.walk.authservice.model.entity.Role;

import java.util.Collection;
import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    List<Role> findAllByNameIn(Collection<RoleName> names);
}
