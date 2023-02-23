package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.EmailValidationException;
import ru.practicum.shareit.exception.UnknownDataException;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceTest {

    private final UserService userService;

    @Test
    @DisplayName("Добавление и получение пользователя")
    void creatAndGetUserTest() {
        UserDto userDto = userService.put(UserDto.builder()
                .name("Liza")
                .email("iva-iva@mail.ru")
                .build()
        );
        assertEquals(userDto, userService.get(userDto.getId()), "не верно получены данные о пользователе");
        assertEquals(List.of(userDto), userService.getAll(), "неверное количество пользователей в базе");
    }

    @Test
    @DisplayName("Добавление и получение пользователя c почтой, которая уже используется")
    void creatUserWithDuplicateEmailTest() {
        UserDto userDto = userService.put(UserDto.builder()
                .name("Liza")
                .email("iva-iva@mail.ru")
                .build()
        );
        assertThrows(EmailValidationException.class, () ->
                userService.put(UserDto.builder()
                        .name("Masha")
                        .email("iva-iva@mail.ru")
                        .build()));
    }

    @Test
    @DisplayName("Обновление данных пользователя")
    void updateUserTest() {
        UserDto userDto = userService.put(UserDto.builder()
                .name("Liza")
                .email("iva-iva@mail.ru")
                .build()
        );

        UserDto updatedUser = userService.update(userDto.getId(), UpdateUserDto.builder()
                .name("Masha")
                .email("iva-ivaiva@bk,ru")
                .build()
        );

        assertEquals(updatedUser, userService.get(userDto.getId()), "Не верное обновление данных");
    }

    @Test
    @DisplayName("Обновление данных пользователя с использованием уже занятой почты")
    void updateUserWithDuplicateEmailTest() {
        UserDto userDto1 = userService.put(UserDto.builder()
                .name("Liza")
                .email("iva-iva@mail.ru")
                .build()
        );

        UserDto userDto2 = userService.put(UserDto.builder()
                .name("Masha")
                .email("ya@mail.ru")
                .build()
        );

        assertThrows(EmailValidationException.class, () ->
                userService.update(userDto2.getId(), UpdateUserDto.builder()
                        .name("Masha")
                        .email("iva-iva@mail.ru")
                        .build()
                ));
    }

    @Test
    @DisplayName("Удаление пользовтаеля")
    void deleteUserTest() {
        UserDto userDto1 = userService.put(UserDto.builder()
                .name("Liza")
                .email("iva-iva@mail.ru")
                .build()
        );
        int id1 = userDto1.getId();

        UserDto userDto2 = userService.put(UserDto.builder()
                .name("Masha")
                .email("ya@mail.ru")
                .build()
        );

        List<UserDto> usersDto1 = userService.getAll();
        assertEquals(2, usersDto1.size(), "Пользователи не сохранены");

        userService.deleteUserById(id1);
        List<UserDto> usersDto2 = userService.getAll();
        assertThrows(UnknownDataException.class, () -> userService.get(id1));
        assertEquals(1, usersDto2.size(), "Пользователь не был удален");
    }
}
