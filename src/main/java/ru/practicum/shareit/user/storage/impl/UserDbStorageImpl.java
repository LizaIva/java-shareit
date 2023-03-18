package ru.practicum.shareit.user.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.UnknownDataException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Component("userDbStorageImpl")
@RequiredArgsConstructor
public class UserDbStorageImpl implements UserStorage {
    private final UserRepository userRepository;

    @Override
    public User put(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User updateUser(User user) {
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UnknownDataException(String.format(USER_NOT_FOUND, id)));
    }

    @Override
    public User deleteById(int id) {
        User user = getUserById(id);
        userRepository.deleteById(id);
        return user;
    }


    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public void checkUser(int id) {
        if (!userRepository.existsById(id)) {
            throw new UnknownDataException(String.format(USER_NOT_FOUND, id));
        }
    }
}
