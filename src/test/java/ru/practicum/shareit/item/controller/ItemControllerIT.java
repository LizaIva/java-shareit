package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.header.HeaderConst.USER_ID_HEADER;


@WebMvcTest(controllers = ItemController.class)
class ItemControllerIT {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;
    @MockBean
    private CommentService commentService;

    @MockBean
    private BookingService bookingService;


    @SneakyThrows
    @Test
    void createItem() {
        int ownerId = 1;
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("Молоток")
                .description("Для гвоздей")
                .available(true)
                .build();

        when(itemService.put(1, itemDto)).thenReturn(itemDto);

        String result = mockMvc.perform(post("/items")
                        .header(USER_ID_HEADER, ownerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertEquals(objectMapper.writeValueAsString(itemDto), result);

        itemDto.setName(null);
        mockMvc.perform(post("/items")
                        .header(USER_ID_HEADER, ownerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto))
                )
                .andExpect(status().isBadRequest());

        itemDto.setName("");
        mockMvc.perform(post("/items")
                        .header(USER_ID_HEADER, ownerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto))
                )
                .andExpect(status().isBadRequest());

        itemDto.setName("Молоток");

        itemDto.setDescription(null);
        mockMvc.perform(post("/items")
                        .header(USER_ID_HEADER, ownerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto))
                )
                .andExpect(status().isBadRequest());

        itemDto.setDescription("");
        mockMvc.perform(post("/items")
                        .header(USER_ID_HEADER, ownerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto))
                )
                .andExpect(status().isBadRequest());

        itemDto.setDescription("Для гвоздей");
        itemDto.setAvailable(null);
        mockMvc.perform(post("/items")
                        .header(USER_ID_HEADER, ownerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto))
                )
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void updateItemNotValid() {
        int itemId = 1;
        int ownerId = 1;

        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("Молоток")
                .description("Для гвоздей")
                .available(true)
                .build();

        UpdatedItemDto updatedItemDto = UpdatedItemDto.builder()
                .name(null)
                .description(null)
                .build();

        String contentAsString = mockMvc.perform(patch("/items/{itemId}", itemId)
                        .header(USER_ID_HEADER, ownerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedItemDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
    }

    //проверка last next booking должна быть в тесте маппера itemMapper, который должен быть unit тестом, тк там нет никаких поисков в бд
    @SneakyThrows
    @Test
    void getItemById() {
        int itemId = 1;
        int userId = 1;
        int bookerId = 2;

        // 1) один из букингов rejected
        // 2) сделать 1 букинг у которого start now -1 день -- это и будет last booking
        // 3) сделать 1 букинг у которого start now +2 день -- этот букинг должен будет замениться следующим (4м)
        // 4) сделать 1 букинг у которого start now +1 день -- это и будет next booking booking
        CreateBookingRequestDto booking0 = CreateBookingRequestDto.builder()
                .itemId(itemId)
                .start(LocalDateTime.of(2023,9,10, 8, 22))
                .end(LocalDateTime.of(2023, 10, 9, 12, 5))
                .build();

        bookingService.put(booking0, bookerId);

        BookingDto bookingDto1 = BookingDto.builder()
                .itemId(itemId)
                .bookerId(bookerId)
                .item(itemService.getItemById(itemId, userId))
                .start(LocalDateTime.of(2023,11,10, 8, 22))
                .end(LocalDateTime.of(2023, 12, 9, 12, 5))
                .build();


        BookingDto bookingDto2 = BookingDto.builder()
                .itemId(itemId)
                .bookerId(bookerId)
                .item(itemService.getItemById(itemId, userId))
                .start(LocalDateTime.of(2023,12,13, 8, 22))
                .end(LocalDateTime.of(2023, 12, 22, 12, 5))
                .build();


        //создать несколько букингов на айтем созданный в createItem
        //создать их таких, чтобы в методе зайдествовать логику работы метода ru.practicum.shareit.item.utils.ItemMapper.mapToItemDto
        // создаем их для того, чтобы проверить, что у нас правильно засунулись букинги в lastBooking and nextBooking



        MockHttpServletRequestBuilder webTestRequest = get("/items/{itemId}", itemId)
                .header(USER_ID_HEADER, userId);

        mockMvc.perform(webTestRequest).andExpect(status().isOk());

        Mockito.verify(itemService).getItemById(itemId, userId);
//        Mockito.verify(itemService, )
    }

    @Test
    void update() {
    }


    @Test
    void getOwnersItems() {
    }

    @Test
    void foundItemWithText() {
    }
}