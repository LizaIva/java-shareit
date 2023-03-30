package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.header.HeaderConst.USER_ID_HEADER;

@ExtendWith(SpringExtension.class)
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
    void createComment() {
        int itemId = 1;
        int userId = 1;
        CreateCommentDto commentDto = CreateCommentDto.builder()
                .text("отличные коньки")
                .build();

        CommentDto createdCommentDto = CommentDto.builder()
                .id(1)
                .text("отличные коньки")
                .build();

        when(commentService.put(itemId, userId, commentDto)).thenReturn(createdCommentDto);

        String result = mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .header(USER_ID_HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        assertNotEquals(objectMapper.writeValueAsString(commentDto), result);


        commentDto.setText("");
        mockMvc.perform(post("/items/{itemId}/comment", itemId)
                        .header(USER_ID_HEADER, userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto))
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


    @SneakyThrows
    @Test
    void getItemByIdAndGetUnknownItemAndGetFromUnknownUser() {
        UserDto userDto = UserDto.builder()
                .id(1)
                .name("Liza")
                .email("ivaiva@mail.ru")
                .build();

        ItemDto itemDto1 = ItemDto.builder()
                .id(1)
                .name("Молоток")
                .description("Для гвоздей")
                .available(true)
                .build();

        ItemDto itemDto2 = ItemDto.builder()
                .id(2)
                .name("Отвертка")
                .description("Строительная")
                .available(true)
                .build();

        when(itemService.put(userDto.getId(), itemDto1)).thenReturn(itemDto1);
        when(itemService.put(userDto.getId(), itemDto2)).thenReturn(itemDto2);

        String result = mockMvc.perform(get("/items/{itemId}", itemDto1.getId())
                        .header(USER_ID_HEADER, userDto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto1))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        assertNotEquals(objectMapper.writeValueAsString(itemDto1), result);

    }

    @Test
    @SneakyThrows
    void update() {
        ItemDto itemDto1 = ItemDto.builder()
                .id(1)
                .name("Молоток")
                .description("Для гвоздей")
                .available(true)
                .build();

        when(itemService.put(1, itemDto1)).thenReturn(itemDto1);

        UpdatedItemDto updatedItemDto = UpdatedItemDto.builder()
                .available(false)
                .build();


        String resultUpdatedItem = mockMvc.perform(patch("/items/{itemId}", itemDto1.getId())
                        .header(USER_ID_HEADER, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedItemDto))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        itemDto1.setAvailable(false);
        assertNotEquals(objectMapper.writeValueAsString(itemDto1), resultUpdatedItem);
    }


    @SneakyThrows
    @Test
    void getOwnersItems() {
        int ownerId = 1;
        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("Молоток")
                .description("Для гвоздей")
                .available(true)
                .build();

        when(itemService.put(1, itemDto)).thenReturn(itemDto);

        ItemDto itemDto2 = ItemDto.builder()
                .id(2)
                .name("Отвертка")
                .description("Для стройки")
                .available(true)
                .build();

        when(itemService.put(1, itemDto2)).thenReturn(itemDto2);

        int ownerId2 = 2;
        ItemDto itemDto3 = ItemDto.builder()
                .id(3)
                .name("Коньки")
                .description("Детские")
                .available(true)
                .build();

        when(itemService.put(2, itemDto3)).thenReturn(itemDto3);

        List<ItemDto> itemsForUser1 = List.of(itemDto, itemDto2);

        String result = mockMvc.perform(get("/items", 5, null)
                        .header(USER_ID_HEADER, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemsForUser1))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        assertNotEquals(objectMapper.writeValueAsString(itemsForUser1), result);


        List<ItemDto> itemsForUser2 = List.of(itemDto3);
        String result2 = mockMvc.perform(get("/items", null, null)
                        .header(USER_ID_HEADER, 2)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemsForUser2))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        assertNotEquals(objectMapper.writeValueAsString(itemsForUser2), result2);

        List<ItemDto> itemsForUser1WithPagination = List.of(itemDto);

        String result3 = mockMvc.perform(get("/items", 1, null)
                        .header(USER_ID_HEADER, 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemsForUser1WithPagination))
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        assertNotEquals(objectMapper.writeValueAsString(itemsForUser1WithPagination), result3);
    }

    @SneakyThrows
    @Test
    void foundItemWithText() {
        int ownerId = 1;
        int ownerId2 = 2;

        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("Молоток")
                .description("Для гвоздей")
                .available(false)
                .build();

        when(itemService.put(1, itemDto)).thenReturn(itemDto);

        ItemDto itemDto2 = ItemDto.builder()
                .id(2)
                .name("Отвертка")
                .description("Как МОЛОток")
                .available(true)
                .build();

        when(itemService.put(2, itemDto2)).thenReturn(itemDto2);

        ItemDto itemDto3 = ItemDto.builder()
                .id(3)
                .name("Коньки")
                .description("Детские")
                .available(true)
                .build();
        when(itemService.put(1, itemDto3)).thenReturn(itemDto3);

        ItemDto itemDto4 = ItemDto.builder()
                .id(4)
                .name("МОЛОТОК")
                .description("обычный")
                .available(true)
                .build();
        when(itemService.put(2, itemDto4)).thenReturn(itemDto4);

        List<ItemDto> itemsDto = List.of(itemDto2, itemDto4);

        when(itemService.foundAvailableItemWithNameOrDescription("МОЛОТОк", null, null)).thenReturn(itemsDto);

        String result = mockMvc.perform(get("/items/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemsDto))
                        .param("text", "МОЛОТОк")
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        assertEquals(objectMapper.writeValueAsString(itemsDto), result);
    }
}