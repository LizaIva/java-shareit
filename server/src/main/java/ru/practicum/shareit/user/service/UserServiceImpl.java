package ru.practicum.shareit.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.utils.UserMapper;

import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(@Qualifier("userDbStorageImpl") UserStorage userStorage, UserMapper userMapper) {
        this.userStorage = userStorage;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto put(UserDto userDto) {
        log.info("Создание пользователя");
        User user = userMapper.mapToUser(userDto);
        User savedUser = userStorage.put(user);
        return userMapper.mapToUserDto(savedUser);
    }

    @Override
    public UserDto update(Integer userId, UpdateUserDto updateUserDto) {
        log.info("Обновление данных пользователя с id {}", userId);

        User user = userStorage.getUserById(userId);

        String name = updateUserDto.getName();
        if (name != null && !name.isEmpty()) {
            user.setName(name);
        }

        String email = updateUserDto.getEmail();
        if (email != null && !email.isEmpty()) {
            user.setEmail(email);
        }

        User updatedUser = userStorage.updateUser(user);
        return userMapper.mapToUserDto(updatedUser);
    }

    @Override
    public UserDto get(Integer id) {
        log.info("Запрос пользователя с id = {}", id);
        User user = userStorage.getUserById(id);
        return userMapper.mapToUserDto(user);
    }

    @Override
    public List<UserDto> getAll() {
        log.info("Запрос всех пользователей");
        List<User> users = userStorage.getAll();
        return userMapper.mapToUsersDto(users);
    }

    @Override
    public UserDto deleteUserById(Integer id) {
        log.info("Запрос на удаление пользователя с id = {}", id);
        User user = userStorage.deleteById(id);
        return userMapper.mapToUserDto(user);
    }
}
