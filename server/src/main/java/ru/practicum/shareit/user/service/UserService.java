package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto put(UserDto userDto);

    UserDto update(Integer userId, UpdateUserDto updateUserDto);

    UserDto get(Integer id);

    List<UserDto> getAll();

    UserDto deleteUserById(Integer id);
}
