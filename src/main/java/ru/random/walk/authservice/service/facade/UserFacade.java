package ru.random.walk.authservice.service.facade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.random.walk.authservice.model.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface UserFacade {
    Page<UserDto> getUsers(List<UUID> ids, Pageable pageable);
}
