package ru.practicum.shareit.server.request.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.server.request.dto.CreateRequestDto;
import ru.practicum.shareit.server.request.dto.RequestDto;
import ru.practicum.shareit.server.request.service.RequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.server.header.HeaderConst.USER_ID_HEADER;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RequestController.class)
class RequestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RequestService requestService;

    @SneakyThrows
    @Test
    void create() {
        int userId = 1;

        CreateRequestDto createRequestDto = CreateRequestDto.builder()
                .description("Срочно нужно платье")
                .build();

        RequestDto expectedRequestDto = RequestDto.builder()
                .id(1)
                .responses(Collections.emptyList())
                .created(LocalDateTime.now())
                .items(Collections.emptyList())
                .description("Срочно нужно платье")
                .build();

        Mockito.when(requestService.put(createRequestDto, userId)).thenReturn(expectedRequestDto);

        String result = mockMvc.perform(post("/requests")
                        .header(USER_ID_HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedRequestDto))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(expectedRequestDto), result);
    }

    @SneakyThrows
    @Test
    void getRequestById() {
        int userId = 1;

        RequestDto expectedRequestDto = RequestDto.builder()
                .id(1)
                .responses(Collections.emptyList())
                .created(LocalDateTime.now())
                .items(Collections.emptyList())
                .description("Срочно нужно платье")
                .build();

        Mockito.when(requestService.get(expectedRequestDto.getId(), userId)).thenReturn(expectedRequestDto);

        String result = mockMvc.perform(get("/requests/{requestId}", expectedRequestDto.getId())
                        .header(USER_ID_HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedRequestDto))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(expectedRequestDto), result);
    }

    @SneakyThrows
    @Test
    void getAll() {
        int userId = 1;

        RequestDto expectedRequestDto = RequestDto.builder()
                .id(1)
                .responses(Collections.emptyList())
                .created(LocalDateTime.now())
                .items(Collections.emptyList())
                .description("Срочно нужно платье")
                .build();

        RequestDto expectedRequestDto2 = RequestDto.builder()
                .id(2)
                .responses(Collections.emptyList())
                .created(LocalDateTime.now())
                .items(Collections.emptyList())
                .description("Возьму в аренду машину")
                .build();

        List<RequestDto> requestsDto = List.of(expectedRequestDto, expectedRequestDto2);

        Mockito.when(requestService.getAll(userId)).thenReturn(requestsDto);

        String result = mockMvc.perform(get("/requests")
                        .header(USER_ID_HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestsDto))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(requestsDto), result);
    }

    @SneakyThrows
    @Test
    void getAllWithOffset() {
        int userId = 1;

        RequestDto expectedRequestDto = RequestDto.builder()
                .id(1)
                .responses(Collections.emptyList())
                .created(LocalDateTime.now())
                .items(Collections.emptyList())
                .description("Срочно нужно платье")
                .build();

        RequestDto expectedRequestDto2 = RequestDto.builder()
                .id(2)
                .responses(Collections.emptyList())
                .created(LocalDateTime.now())
                .items(Collections.emptyList())
                .description("Возьму в аренду машину")
                .build();

        List<RequestDto> requestsDto = List.of(expectedRequestDto, expectedRequestDto2);

        Mockito.when(requestService.getAllWithOffset(5, 0, userId)).thenReturn(requestsDto);

        String result = mockMvc.perform(get("/requests/all")
                        .header(USER_ID_HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestsDto))
                        .param("from", "0")
                        .param("size", "5")
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(requestsDto), result);
    }
}