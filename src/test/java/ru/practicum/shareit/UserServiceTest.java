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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceTest {
    private final UserService userService;

    @Test
    @DisplayName("Добавление и получение пользователя")
    void creatAndGetUserTest(){
        UserDto userDto = userService.put(new UserDto("Liza", "iva-iva@mail.ru"));
        assertEquals(userDto, userService.get(userDto.getId()), "не верно получены данные о пользователе");
        assertEquals(List.of(userDto), userService.getAll(), "неверное количество пользователей в базе");
    }

    @Test
    @DisplayName("Добавление и получение пользователя c почтой, которая уже используется")
    void creatUserWithDuplicateEmailTest(){
        UserDto userDto = userService.put(new UserDto("Liza", "iva-iva@mail.ru"));
        assertThrows(EmailValidationException.class, () ->
                userService.put(new UserDto("Masha", "iva-iva@mail.ru")));
    }

    @Test
    @DisplayName("Обновление данных пользователя")
    void updateUserTest(){
        UserDto userDto = userService.put(new UserDto("Liza", "iva-iva@mail.ru"));
        UserDto updatedUser = userService.update(userDto.getId(), new UpdateUserDto("Masha", "iva-ivaiva@bk,ru"));

        assertEquals(updatedUser, userService.get(userDto.getId()), "Не верное обновление данных");
    }

    @Test
    @DisplayName("Обновление данных пользователя с использованием уже занятой почты")
    void updateUserWithDuplicateEmailTest(){
        UserDto userDto1 = userService.put(new UserDto("Liza", "iva-iva@mail.ru"));

        UserDto userDto2 = userService.put(new UserDto("Masha", "ya@mail.ru"));
        assertThrows(EmailValidationException.class, () ->
                userService.update(userDto2.getId(), new UpdateUserDto("Masha", "iva-iva@mail.ru")));
    }

    @Test
    @DisplayName("Удаление пользовтаеля")
    void deleteUserTest(){
        UserDto userDto1 = userService.put(new UserDto("Liza", "iva-iva@mail.ru"));
        int id1 = userDto1.getId();
        UserDto userDto2 = userService.put(new UserDto("Masha", "ya@mail.ru"));

        List<UserDto> usersDto1 = userService.getAll();
        assertEquals(2, usersDto1.size(), "Пользователи не сохранены");

        userService.deleteUserById(id1);
        List<UserDto> usersDto2 = userService.getAll();
        assertThrows(UnknownDataException.class, () -> userService.get(id1));
        assertEquals(1, usersDto2.size(), "Пользователь не был удален");
    }
}
