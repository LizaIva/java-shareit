//package ru.practicum.shareit;
//
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.DirtiesContext;
//import ru.practicum.shareit.exception.CheckOwnerException;
//import ru.practicum.shareit.item.dto.ItemDto;
//import ru.practicum.shareit.item.dto.UpdatedItemDto;
//import ru.practicum.shareit.item.service.ItemService;
//import ru.practicum.shareit.user.dto.UserDto;
//import ru.practicum.shareit.user.service.UserService;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//class ItemServiceTest {
//
//    private final ItemService itemService;
//    private final UserService userService;
//
//    @Test
//    @DisplayName("Создание предмета")
//    void createAndGetItemTest() {
//        UserDto ownerDto = userService.put(UserDto.builder()
//                .name("Liza")
//                .email("iva-iva@mail.ru")
//                .build()
//        );
//
//        ItemDto itemDto = itemService.put(ownerDto.getId(), ItemDto.builder()
//                .name("дрель")
//                .description("очень мощная")
//                .available(true)
//                .build()
//        );
//
//        assertEquals(itemDto, itemService.getItemById(itemDto.getId()), "не верно получены данные о предмете");
//    }
//
//    @Test
//    @DisplayName("Создание предметов у разных владельцев")
//    void createAndGetItemsFromDifferentOwnersTest() {
//        UserDto userDto1 = userService.put(UserDto.builder()
//                .name("Liza")
//                .email("iva-iva@mail.ru")
//                .build()
//        );
//
//        UserDto userDto2 = userService.put(UserDto.builder()
//                .name("Masha")
//                .email("ya@mail.ru")
//                .build()
//        );
//
//        ItemDto users1ItemDto1 = itemService.put(userDto1.getId(), ItemDto.builder()
//                .name("дрель")
//                .description("очень мощная")
//                .available(true)
//                .build()
//        );
//
//        ItemDto users1ItemDto2 = itemService.put(userDto1.getId(), ItemDto.builder()
//                .name("коньки")
//                .description("как новые")
//                .available(true)
//                .build()
//        );
//
//        ItemDto users2ItemDto1 = itemService.put(userDto2.getId(), ItemDto.builder()
//                .name("самокат")
//                .description("хороший")
//                .available(true)
//                .build()
//        );
//        List<ItemDto> users1Items = itemService.getOwnersItems(userDto1.getId());
//        assertEquals(2, users1Items.size());
//
//        List<ItemDto> users2Items = itemService.getOwnersItems(userDto2.getId());
//        assertEquals(1, users2Items.size());
//    }
//
//    @Test
//    @DisplayName("Обновление статуса предмета")
//    void updateStatusTest() {
//        UserDto ownerDto = userService.put(UserDto.builder()
//                .name("Liza")
//                .email("iva-iva@mail.ru")
//                .build()
//        );
//
//        ItemDto itemDto = itemService.put(ownerDto.getId(), ItemDto.builder()
//                .name("дрель")
//                .description("очень мощная")
//                .available(true)
//                .build()
//        );
//        assertEquals(true, itemDto.getAvailable());
//
//        ItemDto updatedItemDto = itemService.update(ownerDto.getId(), itemDto.getId(), UpdatedItemDto.builder()
//                .name("дрель")
//                .description("очень мощная")
//                .available(false)
//                .build()
//        );
//        assertEquals(false, updatedItemDto.getAvailable());
//    }
//
//    @Test
//    @DisplayName("Обновление статуса предмета НЕ его владельцем")
//    void updateStatusByWrongOwnerTest() {
//        UserDto ownerDto = userService.put(UserDto.builder()
//                .name("Liza")
//                .email("iva-iva@mail.ru")
//                .build()
//        );
//
//        UserDto userDto = userService.put(UserDto.builder()
//                .name("Masha")
//                .email("ya@mail.ru")
//                .build()
//        );
//
//        ItemDto itemDto = itemService.put(ownerDto.getId(), ItemDto.builder()
//                .name("дрель")
//                .description("очень мощная")
//                .available(true)
//                .build()
//        );
//        assertThrows(CheckOwnerException.class, () -> itemService.update(userDto.getId(), itemDto.getId(),
//                UpdatedItemDto.builder()
//                        .name("дрель")
//                        .description("очень мощная")
//                        .available(false)
//                        .build()
//        ));
//    }
//
//    @Test
//    @DisplayName("Поиск предмета")
//    void foundAvailableItemWithNameOrDescriptionTest() {
//        UserDto userDto = userService.put(UserDto.builder()
//                .name("Liza")
//                .email("iva-iva@mail.ru")
//                .build()
//        );
//
//        ItemDto itemDto1 = itemService.put(userDto.getId(), ItemDto.builder()
//                .name("дрель")
//                .description("очень мощная")
//                .available(true)
//                .build());
//
//        ItemDto itemDto2 = itemService.put(userDto.getId(), ItemDto.builder()
//                .name("коньки")
//                .description("как новые")
//                .available(true)
//                .build());
//
//        ItemDto itemDto3 = itemService.put(userDto.getId(), ItemDto.builder()
//                .name("дрель ультра")
//                .description("очень мощная")
//                .available(false)
//                .build());
//
//        ItemDto itemDto4 = itemService.put(userDto.getId(), ItemDto.builder()
//                .name("ДРель")
//                .description("очень мощная")
//                .available(true)
//                .build());
//
//        ItemDto itemDto5 = itemService.put(userDto.getId(), ItemDto.builder()
//                .name("ДРель")
//                .description("дрель очень классная")
//                .available(true)
//                .build());
//
//        List<ItemDto> itemsDto = itemService.foundAvailableItemWithNameOrDescription("дрель");
//        assertEquals(3, itemsDto.size());
//        assertTrue(itemsDto.contains(itemDto1));
//        assertFalse(itemsDto.contains(itemDto2));
//        assertFalse(itemsDto.contains(itemDto3));
//        assertTrue(itemsDto.contains(itemDto4));
//        assertTrue(itemsDto.contains(itemDto5));
//    }
//}
