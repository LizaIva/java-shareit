package ru.practicum.shareit.gateway.request.controller;

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
import ru.practicum.shareit.gateway.request.RequestClient;
import ru.practicum.shareit.gateway.request.RequestController;
import ru.practicum.shareit.gateway.request.dto.CreateRequestDto;
import ru.practicum.shareit.gateway.request.dto.RequestDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.gateway.header.HeaderConst.USER_ID_HEADER;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = RequestController.class)
@Import(RestTemplateConfig.class)
class RequestControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private RequestClient requestService;

    @Autowired
    private RestTemplate restTemplate;

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

        when(restTemplate.exchange(anyString(), eq(HttpMethod.POST), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(ResponseEntity.ok(expectedRequestDto));

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

        expectedRequestDto.setDescription(null);
        mockMvc.perform(post("/requests")
                        .header(USER_ID_HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedRequestDto))
                )
                .andExpect(status().isBadRequest());

        expectedRequestDto.setDescription("");
        mockMvc.perform(post("/requests")
                        .header(USER_ID_HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedRequestDto))
                )
                .andExpect(status().isBadRequest());
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


        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(ResponseEntity.ok(expectedRequestDto));

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

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class)))
                .thenReturn(ResponseEntity.ok(requestsDto));

        String result = mockMvc.perform(get("/requests")
                        .header(USER_ID_HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
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

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(HttpEntity.class), eq(Object.class), any(Map.class)))
                .thenReturn(ResponseEntity.ok(requestsDto));

        String result = mockMvc.perform(get("/requests/all")
                        .header(USER_ID_HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
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