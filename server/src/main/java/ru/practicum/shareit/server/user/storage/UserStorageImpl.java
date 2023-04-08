//package ru.practicum.shareit.gateway.user.storage.impl;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import ru.practicum.shareit.exception.EmailValidationException;
//import ru.practicum.shareit.exception.UnknownDataException;
//import ru.practicum.shareit.gateway.user.model.User;
//import ru.practicum.shareit.gateway.user.storage.UserStorage;
//
//import java.util.*;
//
//@Slf4j
////@Component
//public class UserStorageImpl implements UserStorage {
//    private static final String EMAIL_ALREADY_USED = "email %s уже используется";
//
//    private static final String NOT_EXIST = "Нельзя сохранить пользователя без данных";
//
//    private final Map<Integer, User> users = new HashMap<>();
//    private final Set<String> emails = new HashSet<>();
//
//    private int counter = 0;
//
//    @Override
//    public User put(User user) {
//        if (user == null) {
//            throw new UnknownDataException(NOT_EXIST);
//        }
//        checkUserEmail(user.getEmail());
//
//        if (user.getId() == null) {
//            user.setId(++counter);
//        }
//        users.put(user.getId(), user);
//        return user;
//    }
//
//    @Override
//    public User updateUser(User user) {
//        checkUser(user.getId());
//        User oldUser = users.get(user.getId());
//        if (user.getEmail() != null && !oldUser.getEmail().equals(user.getEmail())) {
//            checkUserEmail(user.getEmail());
//            emails.remove(oldUser.getEmail());
//            emails.add(user.getEmail());
//            oldUser.setEmail(user.getEmail());
//        }
//        if (user.getName() != null) {
//            oldUser.setName(user.getName());
//        }
//
//        return oldUser;
//    }
//
//    @Override
//    public User getUserById(Integer id) {
//        checkUser(id);
//        return users.get(id);
//    }
//
//
//    public List<User> getUsersByIds(List<Integer> ids) {
//        List<User> usersList = new ArrayList<>();
//        for (Integer id : ids) {
//            checkUser(id);
//            usersList.add(users.get(id));
//        }
//        return usersList;
//    }
//
//    @Override
//    public List<User> getAll() {
//        return new ArrayList<>(users.values());
//    }
//
//    @Override
//    public User deleteById(int id) {
//        checkUser(id);
//        emails.remove(users.get(id).getEmail());
//        return users.remove(id);
//    }
//
//    @Override
//    public void checkUser(int id) {
//        User user = users.get(id);
//        if (user == null) {
//            log.info("user с id = {} не найден.", id);
//            throw new UnknownDataException(String.format(USER_NOT_FOUND, id));
//        }
//    }
//
//    private void checkUserEmail(String email) {
//        if (!emails.add(email)) {
//            log.info("email {} уже существует.", email);
//            throw new EmailValidationException(String.format(EMAIL_ALREADY_USED, email));
//        }
//    }
//}
