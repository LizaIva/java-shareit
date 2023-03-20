package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {

    String USER_NOT_FOUND = "user с id = %s не найден.";

    User put(User user);

    User updateUser(User user);

    User getUserById(Integer id);

    User deleteById(int id);

    List<User> getAll();

    void checkUser(int id);
}
