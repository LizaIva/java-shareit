package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.utils.UserMapper;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final UserMapper userMapper;

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

        User updatedUser = userStorage.updateUser(userId, updateUserDto);
        return userMapper.mapToUserDto(updatedUser);
    }

    @Override
    public UserDto get(Integer id) {
        log.info("Запрос пользователя с id = {}", id);
        User user = userStorage.getUserById(id);
        return userMapper.mapToUserDto(user);
    }

    @Override
    public List<UserDto> getUsersByIds(List<Integer> ids) {
        log.info("Запрос пользователей по их id");
        List<User> users = userStorage.getUsersByIds(ids);
        return userMapper.mapToUsersDto(users);
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
