package ru.random.walk.authservice.service.facade.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.random.walk.authservice.model.dto.UserDto;
import ru.random.walk.authservice.service.UserService;
import ru.random.walk.authservice.service.facade.UserFacade;
import ru.random.walk.authservice.service.mapper.AuthUserMapper;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade {

    private final UserService userService;
    private final AuthUserMapper userMapper;

    @Override
    public Page<UserDto> getUsers(List<UUID> ids, Pageable pageable) {
        return userService.getUsersPage(ids, pageable)
                .map(userMapper::toUserDto);
    }
}
