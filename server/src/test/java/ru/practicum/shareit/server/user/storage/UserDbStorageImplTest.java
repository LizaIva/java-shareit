package ru.practicum.shareit.server.user.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.practicum.shareit.server.exception.UnknownDataException;
import ru.practicum.shareit.server.user.dto.UpdateUserDto;
import ru.practicum.shareit.server.user.dto.UserDto;
import ru.practicum.shareit.server.user.service.UserService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageImplTest {

    private final UserService userService;
    private final JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM users");
    }

    @Test
    void putAndPutUserWithDuplicateEmail() {
        UserDto userDto = userService.put(UserDto.builder()
                .name("Liza")
                .email("ivaiva@bk.ru")
                .build());
        assertEquals(userDto, userService.get(userDto.getId()), "Не верно получены данные о пользователе");
        assertEquals(Arrays.asList(userDto), userService.getAll(),
                "Неверное количество пользователей в базе");

        UserDto userDtoWithDuplicateEmail = UserDto.builder()
                .name("Masha")
                .email("ivaiva@bk.ru")
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> userService.put(userDtoWithDuplicateEmail));
    }


    @Test
    void updateUser() {
        UserDto userDto = userService.put(UserDto.builder()
                .name("Liza")
                .email("ivaiva@bk.ru")
                .build());
        assertEquals(userDto, userService.get(userDto.getId()), "Не верно получены данные о пользователе");

        UpdateUserDto updateUserDto = UpdateUserDto.builder()
                .name("Masha")
                .email("kjhgkga@mail.ru")
                .build();

        UserDto actualUserDto = userService.update(userDto.getId(), updateUserDto);
        assertEquals(actualUserDto, userService.get(userDto.getId()), "Не верно получены данные о пользователе");
    }

    @Test
    void updateUserWithDuplicateEmail() {
        UserDto userDto = userService.put(UserDto.builder()
                .name("Liza")
                .email("ivaiva@bk.ru")
                .build());
        assertEquals(userDto, userService.get(userDto.getId()), "Не верно получены данные о пользователе");

        UserDto userDto1 = userService.put(UserDto.builder()
                .name("Masha")
                .email("ghujkb@mail.ru")
                .build());

        UpdateUserDto updateUserDto = UpdateUserDto.builder()
                .email("ivaiva@bk.ru")
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> userService.update(userDto1.getId(), updateUserDto));
    }

    @Test
    void getByIdAndGetUserByWrongId() {
        UserDto userDto = userService.put(UserDto.builder()
                .name("Liza")
                .email("ivaiva@bk.ru")
                .build());
        assertEquals(userDto, userService.get(userDto.getId()), "Не верно получены данные о пользователе");

        assertThrows(UnknownDataException.class, () -> userService.get(999));
    }

    @Test
    void deleteById() {
        UserDto userDto = userService.put(UserDto.builder()
                .name("Liza")
                .email("ivaiva@bk.ru")
                .build());
        assertEquals(userDto, userService.get(userDto.getId()), "Не верно получены данные о пользователе");

        UserDto userDto1 = userService.put(UserDto.builder()
                .name("Masha")
                .email("ghujkb@mail.ru")
                .build());
        assertEquals(userDto1, userService.get(userDto1.getId()), "Не верно получены данные о пользователе");

        userService.deleteUserById(userDto.getId());

        assertThrows(UnknownDataException.class, () -> userService.get(userDto.getId()));
    }

    @Test
    void getAll() {
        UserDto userDto = userService.put(UserDto.builder()
                .name("Liza")
                .email("ivaiva@bk.ru")
                .build());

        UserDto userDto1 = userService.put(UserDto.builder()
                .name("Masha")
                .email("ghujkb@mail.ru")
                .build());
        List<UserDto> users = userService.getAll();

        assertEquals(2, users.size());

        assertEquals(userDto, users.get(0));
        assertEquals(userDto1, users.get(1));
    }

    @Test
    void checkUser() {
        assertThrows(UnknownDataException.class, () -> userService.get(999));
    }
}