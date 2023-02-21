package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    User put(User user);

    User updateUser(Integer userId, UpdateUserDto updateUserDto);

    User getUserById(Integer id);

    User deleteById(int id);

    List<User> getUsersByIds(List<Integer> ids);

    List<User> getAll();

    void checkUser(int id);
}
