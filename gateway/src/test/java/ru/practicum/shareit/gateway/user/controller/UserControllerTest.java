package ru.practicum.shareit.gateway.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import ru.practicum.shareit.gateway.config.RestTemplateConfig;
import ru.practicum.shareit.gateway.user.UserClient;
import ru.practicum.shareit.gateway.user.UserController;
import ru.practicum.shareit.gateway.user.dto.UpdateUserDto;
import ru.practicum.shareit.gateway.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
@Import(RestTemplateConfig.class)
class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private UserClient userClient;

    @Autowired
    private RestTemplate restTemplate;

    @SneakyThrows
    @Test
    void createAndCreateWithWrongEmailAndEmptyName() {
        UserDto userDto = UserDto.builder()
                .id(1)
                .name("Liza")
                .email("ivaiva@mail.ru")
                .build();

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(ResponseEntity.ok(userDto));

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
                .name("Masha")
                .email("kjhglh@mail.ru")
                .build();

        when(restTemplate.exchange(anyString(), eq(HttpMethod.PATCH), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(ResponseEntity.ok(userDto));

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

        assertEquals(objectMapper.writeValueAsString(userDto), resultUpdatedUser);
    }

    @SneakyThrows
    @Test
    void getUserById() {
        UserDto userDto = UserDto.builder()
                .id(1)
                .name("Liza")
                .email("ivaiva@mail.ru")
                .build();

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(ResponseEntity.ok(userDto));

        String result = mockMvc.perform(get("/users/{userId}", userDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        assertEquals(objectMapper.writeValueAsString(userDto), result);
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

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(ResponseEntity.ok(usersDto));


        String result = mockMvc.perform(get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
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

        when(restTemplate.exchange(anyString(), eq(HttpMethod.DELETE), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(ResponseEntity.ok(userDto));

        String deletedUser = mockMvc.perform(delete("/users/{id}", userDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(userDto), deletedUser);
    }
}