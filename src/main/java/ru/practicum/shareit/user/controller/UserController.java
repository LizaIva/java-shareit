package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserDto create(@RequestBody @Valid UserDto userDto) {
        log.info("Получен запрос на создание пользователя");
        return userService.put(userDto);
    }

    @PatchMapping("/{userId}")
    public UserDto update(@PathVariable Integer userId,
                          @RequestBody @Valid UpdateUserDto update) {
        log.info("Получен запрос на обновление пользователя");
        return userService.update(userId, update);
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable Integer userId) {
        log.info("Получен запрос на вывод пользователя с id = {}", userId);
        return userService.get(userId);
    }

    @GetMapping("/list/{ids}")
    public List<UserDto> getUsersByIds(@PathVariable Integer[] ids) {
        log.info("Получен запрос на вывод нескольких пользователей по их id");
        return userService.getUsersByIds(List.of(ids));
    }

    @GetMapping
    public Collection<UserDto> getAllUsers() {
        log.info("Получен запрос на вывод списка всех пользователей");
        return userService.getAll();
    }

    @DeleteMapping("/{id}")
    public UserDto deleteUserById(@PathVariable int id) {
        log.info("Получен запрос на удаление пользоваля с id = {}", id);
        return userService.deleteUserById(id);
    }
}


