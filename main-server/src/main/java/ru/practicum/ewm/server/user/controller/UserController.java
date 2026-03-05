package ru.practicum.ewm.server.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.server.user.DTO.NewUserRequest;
import ru.practicum.ewm.server.user.DTO.UserDto;
import ru.practicum.ewm.server.user.mapper.UserMapper;
import ru.practicum.ewm.server.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@Valid @RequestBody NewUserRequest user) {
        return userMapper.toUserDto(userService.addUser(userMapper.toUser(user)));
    }

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(required = false) String[] ids,
                                  @RequestParam(defaultValue = "0") int from,
                                  @RequestParam(defaultValue = "10") int size) {
        return userService.getUsers(ids, from, size).stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        userService.removeUser(userId);
    }
}
