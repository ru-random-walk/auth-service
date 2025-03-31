package ru.random.walk.authservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.random.walk.authservice.model.dto.DetailedUserDto;
import ru.random.walk.authservice.model.dto.UserDto;
import ru.random.walk.authservice.service.facade.UserFacade;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserFacade userFacade;

    @Operation(summary = "Get users info by their ids")
    @GetMapping("/users")
    public Page<UserDto> getUsers(
            @RequestParam("ids") List<UUID> ids,
            @ParameterObject Pageable pageable
    ) {
        return userFacade.getUsers(ids, pageable);
    }

    @Operation(summary = "Get info about current user")
    @GetMapping("/userinfo/me")
    public DetailedUserDto getSelfInfo(Principal principal) {
        return userFacade.getSelfInfo(principal.getName());
    }
}
