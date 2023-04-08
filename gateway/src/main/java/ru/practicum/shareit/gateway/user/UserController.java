package ru.practicum.shareit.gateway.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.user.dto.UpdateUserDto;
import ru.practicum.shareit.gateway.user.dto.UserDto;

import javax.validation.Valid;


@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid UserDto userDto) {
        log.info("Получен запрос на создание пользователя");
        return userClient.createUser(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@PathVariable Integer userId,
                                         @RequestBody UpdateUserDto update) {
        log.info("Получен запрос на обновление пользователя");
        return userClient.updateUser(userId, update);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable Integer userId) {
        log.info("Получен запрос на вывод пользователя с id = {}", userId);
        return userClient.getUser(userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Получен запрос на вывод списка всех пользователей");
        return userClient.getAllUsers();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUserById(@PathVariable int id) {
        log.info("Получен запрос на удаление пользоваля с id = {}", id);
        return userClient.deleteUserById(id);
    }
}
