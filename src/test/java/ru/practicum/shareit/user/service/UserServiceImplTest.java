package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.UnknownDataException;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import ru.practicum.shareit.user.utils.UserMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserStorage userStorage;
    @Spy
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void putAndGetById() {
        User user = new User();
        user.setId(1);
        user.setName("Liza");
        user.setEmail("iva@mail.ru");

        Mockito.when(userStorage.getUserById(user.getId())).thenReturn(user);

        UserDto actualUserDto = userService.get(user.getId());

        assertEquals(1, actualUserDto.getId());
        assertEquals("Liza", actualUserDto.getName());
        assertEquals("iva@mail.ru", actualUserDto.getEmail());
    }

    @Test
    void putWithWrongEmailAndGetById() {
        User user = new User();
        user.setId(1);
        user.setName("Liza");
        user.setEmail("ivamail.ru");

        Mockito.when(userStorage.put(user)).thenThrow(ru.practicum.shareit.exception.ValidationException.class);

        assertThrows(ru.practicum.shareit.exception.ValidationException.class, () -> userStorage.put(user));
    }

    @Test
    void putWithEmptyEmailAndGetById() {
        User user = new User();
        user.setId(1);
        user.setName("Liza");
        user.setEmail("");

        Mockito.when(userStorage.put(user)).thenThrow(ru.practicum.shareit.exception.ValidationException.class);

        assertThrows(ru.practicum.shareit.exception.ValidationException.class, () -> userStorage.put(user));
    }

    @Test
    void update() {
        User user = new User();
        user.setId(1);
        user.setName("Liza");
        user.setEmail("iva@mail.ru");

        User updatedUser = new User();
        updatedUser.setId(1);
        updatedUser.setName("Masha");
        updatedUser.setEmail("iva@mail.ru");

        Mockito.when(userStorage.getUserById(user.getId())).thenReturn(user);
        Mockito.when(userStorage.updateUser(user)).thenReturn(updatedUser);

        UpdateUserDto updateUserDto = new UpdateUserDto("Masha", "iva@mail.ru");

        UserDto actual = userService.update(user.getId(), updateUserDto);

        assertEquals("Masha", actual.getName());
        assertEquals("iva@mail.ru", actual.getEmail());
    }

    @Test
    void getAll() {

        User user = new User();
        user.setId(1);
        user.setName("Liza");
        user.setEmail("iva@mail.ru");

        List<User> users = List.of(user);
        Mockito.when(userStorage.getAll()).thenReturn(users);

        List<UserDto> actual = userService.getAll();

        assertEquals(1, actual.size());

        UserDto actualUserDto = actual.get(0);

        assertEquals(1, actualUserDto.getId());
        assertEquals("Liza", actualUserDto.getName());
        assertEquals("iva@mail.ru", actualUserDto.getEmail());


        User user2 = new User();
        user2.setId(2);
        user2.setName("Masha");
        user2.setEmail("petrova@mail.ru");

        List<User> users2 = List.of(user, user2);
        Mockito.when(userStorage.getAll()).thenReturn(users2);

        List<UserDto> actual2 = userService.getAll();

        assertEquals(2, actual2.size());

        UserDto actualUserDto2 = actual2.get(1);

        assertEquals(2, actualUserDto2.getId());
        assertEquals("Masha", actualUserDto2.getName());
        assertEquals("petrova@mail.ru", actualUserDto2.getEmail());
    }

    @Test
    void getByIdUnknownUser() {
        User user = new User();
        user.setId(1);
        user.setName("Liza");
        user.setEmail("iva@mail.ru");

        Mockito.when(userStorage.getUserById(user.getId())).thenReturn(user);

        UserDto actualUserDto = userService.get(user.getId());

        assertEquals(1, actualUserDto.getId());
        assertEquals("Liza", actualUserDto.getName());
        assertEquals("iva@mail.ru", actualUserDto.getEmail());

        Mockito.when(userStorage.getUserById(99)).thenThrow(UnknownDataException.class);
        assertThrows(UnknownDataException.class, () -> userService.get(99));
    }
}