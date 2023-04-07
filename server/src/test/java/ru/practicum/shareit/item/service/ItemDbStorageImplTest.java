package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.CheckOwnerException;
import ru.practicum.shareit.exception.UnknownDataException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemDbStorageImplTest {

    private final ItemService itemService;
    private final UserService userService;

    @Test
    void putAndPutWithUnknownUserAndWithEmptyNameAndDescriptionAndAvailable() {
        UserDto userDto = userService.put(UserDto.builder()
                .id(1)
                .name("Liza")
                .email("iva@mail.ru")
                .build());

        ItemDto actual = itemService.put(userDto.getId(),
                ItemDto.builder()
                        .id(1)
                        .name("Молоток")
                        .description("Для гвоздей")
                        .available(true)
                        .comments(null)
                        .build()
        );

        ItemDto expected = ItemDto.builder()
                .id(1)
                .name("Молоток")
                .description("Для гвоздей")
                .available(true)
                .comments(null)
                .build();

        assertEquals(expected, actual);

        assertThrows(UnknownDataException.class, () -> itemService.put(999,
                ItemDto.builder()
                        .id(2)
                        .name("Отвертка")
                        .description("Строителтьная")
                        .available(true)
                        .comments(null)
                        .build()));

        assertThrows(DataIntegrityViolationException.class, () -> itemService.put(userDto.getId(),
                ItemDto.builder()
                        .id(2)
                        .name(null)
                        .description("Строителтьная")
                        .available(true)
                        .comments(null)
                        .build()));

        assertThrows(DataIntegrityViolationException.class, () -> itemService.put(userDto.getId(),
                ItemDto.builder()
                        .id(2)
                        .name("Отвертка")
                        .description(null)
                        .available(true)
                        .comments(null)
                        .build()));

        assertThrows(DataIntegrityViolationException.class, () -> itemService.put(userDto.getId(),
                ItemDto.builder()
                        .id(2)
                        .name("Отвертка")
                        .description("Строителтьная")
                        .available(null)
                        .comments(null)
                        .build()));
    }

    @Test
    void getItemByIdAndGetUnknownUser() {
        UserDto userDto = userService.put(UserDto.builder()
                .id(1)
                .name("Liza")
                .email("iva@mail.ru")
                .build());

        ItemDto actual = itemService.put(userDto.getId(),
                ItemDto.builder()
                        .id(1)
                        .name("Молоток")
                        .description("Для гвоздей")
                        .available(true)
                        .comments(null)
                        .build()
        );

        ItemDto expected = ItemDto.builder()
                .id(1)
                .name("Молоток")
                .description("Для гвоздей")
                .available(true)
                .comments(Collections.emptyList())
                .build();

        assertEquals(expected, itemService.getItemById(actual.getId(), userDto.getId()));

        assertThrows(UnknownDataException.class, () -> itemService.getItemById(actual.getId(), 999));
    }

    @Test
    void getAllOwnersItemsAndGetItemsForUnknownUserAndWithIllegalSizeAnfFrom() {
        UserDto userDto = userService.put(UserDto.builder()
                .id(1)
                .name("Liza")
                .email("iva@mail.ru")
                .build());

        ItemDto ownerItem1 = ItemDto.builder()
                .id(1)
                .name("Молоток")
                .description("Для гвоздей")
                .available(true)
                .comments(Collections.emptyList())
                .build();
        itemService.put(userDto.getId(), ownerItem1);

        ItemDto ownerItem2 = ItemDto.builder()
                .id(2)
                .name("Отвертка")
                .description("Строительная")
                .available(true)
                .comments(Collections.emptyList())
                .build();
        itemService.put(userDto.getId(), ownerItem2);

        ItemDto ownerItem3 = ItemDto.builder()
                .id(3)
                .name("Cкалка")
                .description("Для готовки")
                .available(true)
                .comments(Collections.emptyList())
                .build();
        itemService.put(userDto.getId(), ownerItem3);

        UserDto userDto1 = userService.put(UserDto.builder()
                .id(2)
                .name("Masha")
                .email("abc@mail.ru")
                .build());

        ItemDto differentItem = ItemDto.builder()
                .id(4)
                .name("Молоток")
                .description("Для гвоздей")
                .available(true)
                .comments(Collections.emptyList())
                .build();
        itemService.put(userDto1.getId(), differentItem);

        List<ItemDto> actualItems = itemService.getOwnersItems(userDto.getId(), 5, null);
        List<ItemDto> ownerItems = List.of(ownerItem1, ownerItem2, ownerItem3);

        assertEquals(ownerItems, actualItems);

        List<ItemDto> actualItems1 = itemService.getOwnersItems(userDto1.getId(), 5, null);
        List<ItemDto> ownerItems1 = List.of(differentItem);
        assertEquals(ownerItems1, actualItems1);
        assertEquals(ownerItems1.get(0), actualItems1.get(0));

        assertThrows(UnknownDataException.class, () -> itemService.getOwnersItems(99, null, null));

        assertThrows(IllegalArgumentException.class, () -> itemService.getOwnersItems(userDto1.getId(), -1, -1));
    }

    @Test
    void foundAvailableItemWithNameOrDescription() {
        UserDto userDto = userService.put(UserDto.builder()
                .name("Liza")
                .email("iva-iva@mail.ru")
                .build()
        );

        ItemDto itemDto1 = ItemDto.builder()
                .id(1)
                .name("дрель")
                .description("очень мощная")
                .available(true)
                .comments(Collections.emptyList())
                .build();
        itemService.put(userDto.getId(), itemDto1);

        ItemDto itemDto2 = ItemDto.builder()
                .id(2)
                .name("коньки")
                .description("как новые")
                .available(true)
                .comments(Collections.emptyList())
                .build();
        itemService.put(userDto.getId(), itemDto2);

        ItemDto itemDto3 = ItemDto.builder()
                .id(3)
                .name("дрель ультра")
                .description("очень мощная")
                .available(false)
                .comments(Collections.emptyList())
                .build();
        itemService.put(userDto.getId(), itemDto3);

        ItemDto itemDto4 = ItemDto.builder()
                .id(4)
                .name("ДРель")
                .description("очень мощная")
                .available(true)
                .comments(Collections.emptyList())
                .build();
        itemService.put(userDto.getId(), itemDto4);

        ItemDto itemDto5 = ItemDto.builder()
                .id(5)
                .name("ДРель")
                .description("дрель очень классная")
                .available(true)
                .comments(Collections.emptyList())
                .build();
        itemService.put(userDto.getId(), itemDto5);

        List<ItemDto> expectedItems = List.of(itemDto1, itemDto4, itemDto5);

        List<ItemDto> itemsDto = itemService.foundAvailableItemWithNameOrDescription("ДРелЬ", 10, null);
        assertEquals(3, itemsDto.size());
        assertEquals(expectedItems, itemsDto);
        assertEquals(expectedItems.get(0), itemsDto.get(0));
        assertEquals(expectedItems.get(1), itemsDto.get(1));
        assertEquals(expectedItems.get(2), itemsDto.get(2));

        List<ItemDto> expectedItems2 = List.of(itemDto1, itemDto4);

        List<ItemDto> actualSearch = itemService.foundAvailableItemWithNameOrDescription("ДРелЬ", 2, 0);
        assertEquals(2, actualSearch.size());
        assertEquals(expectedItems2, actualSearch);
    }

    @Test
    void updateItemAndUpdatedFromNotOwner() {
        UserDto userDto = userService.put(UserDto.builder()
                .name("Liza")
                .email("iva-iva@mail.ru")
                .build()
        );

        ItemDto ownerItem1 = ItemDto.builder()
                .id(1)
                .name("Молоток")
                .description("Для гвоздей")
                .available(true)
                .comments(Collections.emptyList())
                .build();
        itemService.put(userDto.getId(), ownerItem1);

        ItemDto updatedItemDto = itemService.update(userDto.getId(), ownerItem1.getId(), UpdatedItemDto.builder()
                .available(false)
                .build());

        assertEquals(false, updatedItemDto.getAvailable());

        UserDto userDto1 = userService.put(UserDto.builder()
                .name("Masha")
                .email("abc@mail.ru")
                .build()
        );

        assertThrows(CheckOwnerException.class, () -> itemService.update(userDto1.getId(), ownerItem1.getId(),
                UpdatedItemDto.builder()
                        .available(true)
                        .build()));
    }

    @Test
    void checkItem() {
        UserDto userDto = UserDto.builder()
                .id(1)
                .name("Liza")
                .email("iva-iva@mail.ru")
                .build();

        userService.put(userDto);

        ItemDto ownerItem1 = ItemDto.builder()
                .id(1)
                .name("Молоток")
                .description("Для гвоздей")
                .available(true)
                .comments(Collections.emptyList())
                .build();

        assertThrows(UnknownDataException.class, () -> itemService.getItemById(99, userDto.getId()));
    }
}