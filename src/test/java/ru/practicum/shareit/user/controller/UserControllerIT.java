package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerIT {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @SneakyThrows
    @Test
    void createAndCreateWithWrongEmailAndEmptyName() {
        UserDto userDto = UserDto.builder()
                .id(1)
                .name("Liza")
                .email("ivaiva@mail.ru")
                .build();

        when(userService.put(userDto)).thenReturn(userDto);

        String result = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(userDto), result);

        userDto.setName("");
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto))
                )
                .andExpect(status().isBadRequest());

        userDto.setName("Liza");
        userDto.setEmail("kjlhlh_mail.ru");
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto))
                )
                .andExpect(status().isBadRequest());

        userDto.setEmail("");
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto))
                )
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void update() {
        UserDto userDto = UserDto.builder()
                .id(1)
                .name("Liza")
                .email("ivaiva@mail.ru")
                .build();

        when(userService.put(userDto)).thenReturn(userDto);

        String result = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);


        UpdateUserDto updateUserDto = UpdateUserDto.builder()
                .name("Masha")
                .email("kjhglh@mail.ru")
                .build();

        String resultUpdatedUser = mockMvc.perform(patch("/users/{userId}", userDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDto))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);


        assertNotEquals(objectMapper.writeValueAsString(userDto), resultUpdatedUser);

        String resultGetUpdatedUser = mockMvc.perform(get("/users/{userId}", userDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDto))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(resultUpdatedUser, resultGetUpdatedUser);

        updateUserDto.setEmail("kjlhlh_mail.ru");
        mockMvc.perform(patch("/users{userId}", userDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDto))
                )
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void getUserByIdAndGetUnknownUserId() {
        UserDto userDto = UserDto.builder()
                .id(1)
                .name("Liza")
                .email("ivaiva@mail.ru")
                .build();

        when(userService.put(userDto)).thenReturn(userDto);

        String result = mockMvc.perform(get("/users/{userId}", userDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        assertNotEquals(objectMapper.writeValueAsString(userDto), result);

        mockMvc.perform(get("/users{userId}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto))
                )
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void getAllUsers() {
        UserDto userDto = UserDto.builder()
                .id(1)
                .name("Liza")
                .email("ivaiva@mail.ru")
                .build();


        UserDto userDto2 = UserDto.builder()
                .id(2)
                .name("Masha")
                .email("abc@mail.ru")
                .build();

        List<UserDto> usersDto = List.of(userDto, userDto2);

        when(userService.getAll()).thenReturn(usersDto);

        String result = mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usersDto))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(usersDto), result);
    }

    @SneakyThrows
    @Test
    void deleteUserById() {
        UserDto userDto = UserDto.builder()
                .id(1)
                .name("Liza")
                .email("ivaiva@mail.ru")
                .build();


        UserDto userDto2 = UserDto.builder()
                .id(2)
                .name("Masha")
                .email("abc@mail.ru")
                .build();

        List<UserDto> usersDto = List.of(userDto, userDto2);

        when(userService.getAll()).thenReturn(usersDto);

        String result = mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usersDto))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(usersDto), result);


        when(userService.deleteUserById(userDto.getId())).thenReturn(userDto);
        String deletedUser = mockMvc.perform(delete("/users/{id}", userDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(userDto), deletedUser);


        List<UserDto> usersDtoAfterDelete = List.of(userDto2);
        when(userService.getAll()).thenReturn(usersDtoAfterDelete);


        String resultAfterDelete = mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(usersDtoAfterDelete))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(usersDtoAfterDelete), resultAfterDelete);
    }
}