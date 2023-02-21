package ru.practicum.shareit.user.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.EmailValidationException;
import ru.practicum.shareit.exception.UnknownDataException;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.*;

@Slf4j
@Component
public class UserStorageImpl implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    private int counter = 0;

    @Override
    public User put(User user) {
        if (user == null) {
            throw new UnknownDataException("Нельзя сохранить пользователя без данных");
        }
        checkUserEmail(user.getEmail());

        if (user.getId() == null) {
            user.setId(++counter);
        }
        users.put(user.getId(), user);
        return user;

    }

    @Override
    public User updateUser(Integer userId, UpdateUserDto updateUserDto) {
        checkUser(userId);
        User oldUser = users.get(userId);
        if (updateUserDto.getEmail() != null && !oldUser.getEmail().equals(updateUserDto.getEmail())) {
            checkUserEmail(updateUserDto.getEmail());
            emails.remove(oldUser.getEmail());
            emails.add(updateUserDto.getEmail());
            oldUser.setEmail(updateUserDto.getEmail());
        }
        if (updateUserDto.getName() != null) {
            oldUser.setName(updateUserDto.getName());
        }

        return oldUser;
    }

    @Override
    public User getUserById(Integer id) {
        checkUser(id);
        return users.get(id);
    }

    @Override
    public List<User> getUsersByIds(List<Integer> ids) {
        List<User> usersList = new ArrayList<>();
        for (Integer id : ids) {
            checkUser(id);
            usersList.add(users.get(id));
        }
        return usersList;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User deleteById(int id) {
        checkUser(id);
        emails.remove(users.get(id).getEmail());
        return users.remove(id);
    }

    @Override
    public void checkUser(int id) {
        User user = users.get(id);
        if (user == null) {
            log.info("user с id = {} не найден.", id);
            throw new UnknownDataException("user с id = " + id + " не найден.");
        }
    }

    private void checkUserEmail(String email) {
        if (!emails.add(email)) {
            log.info("email {} уже существует.", email);
            throw new EmailValidationException("email " + email + " уже используется");
        }
    }
}
