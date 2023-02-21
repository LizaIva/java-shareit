package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.CheckOwnerException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ItemServiceTest {
    private final ItemService itemService;
    private final UserService userService;

    @Test
    @DisplayName("Создание предмета")
    void createAndGetItemTest() {
        UserDto userDto = userService.put(new UserDto("Liza", "iva-iva@mail.ru"));
        ItemDto itemDto = itemService.put(userDto.getId(), new ItemDto("дрель", "очень мощная", true));
        assertEquals(itemDto, itemService.getItemById(itemDto.getId()), "не верно получены данные о предмете");
    }

    @Test
    @DisplayName("Создание предметов у разных владельцев")
    void createAndGetItemsFromDifferentOwnersTest() {
        UserDto userDto1 = userService.put(new UserDto("Liza", "iva-iva@mail.ru"));
        UserDto userDto2 = userService.put(new UserDto("Masha", "ya@mail.ru"));

        ItemDto users1ItemDto1 = itemService.put(userDto1.getId(), new ItemDto("дрель", "очень мощная", true));
        ItemDto users1ItemDto2 = itemService.put(userDto1.getId(), new ItemDto("коньки", "как новые", true));

        ItemDto users2ItemDto1 = itemService.put(userDto2.getId(), new ItemDto("самокат", "супер быстрый", true));

        List<ItemDto> users1Items = itemService.getOwnersItems(userDto1.getId());
        assertEquals(2, users1Items.size());

        List<ItemDto> users2Items = itemService.getOwnersItems(userDto2.getId());
        assertEquals(1, users2Items.size());
    }

    @Test
    @DisplayName("Обновление статуса предмета")
    void updateStatusTest() {
        UserDto userDto = userService.put(new UserDto("Liza", "iva-iva@mail.ru"));
        ItemDto itemDto = itemService.put(userDto.getId(), new ItemDto("дрель", "очень мощная", true));
        assertEquals(true, itemDto.getAvailable());

        ItemDto updatedItemDto = itemService.update(userDto.getId(), itemDto.getId(), new UpdatedItemDto("дрель", "очень мощная", false));
        assertEquals(false, updatedItemDto.getAvailable());

    }

    @Test
    @DisplayName("Обновление статуса предмета НЕ его владельцем")
    void updateStatusByWrongOwnerTest() {
        UserDto ownerDto = userService.put(new UserDto("Liza", "iva-iva@mail.ru"));
        UserDto userDto = userService.put(new UserDto("Nikita", "iva@mail.ru"));
        ItemDto itemDto = itemService.put(ownerDto.getId(), new ItemDto("дрель", "очень мощная", true));
        assertThrows(CheckOwnerException.class, () -> itemService.update(userDto.getId(), itemDto.getId(),
                new UpdatedItemDto("дрель", "очень мощная", false)));
    }

    @Test
	@DisplayName("Поиск предмета")
    void foundAvailableItemWithNameOrDescriptionTest() {
		UserDto userDto = userService.put(new UserDto("Liza", "iva-iva@mail.ru"));

		ItemDto itemDto1 = itemService.put(userDto.getId(), new ItemDto("дрель", "очень мощная", true));
		ItemDto itemDto2 = itemService.put(userDto.getId(), new ItemDto("коньки", "как новые", true));
		ItemDto itemDto3 = itemService.put(userDto.getId(), new ItemDto("дрель ультра", "очень мощная", false));
		ItemDto itemDto4 = itemService.put(userDto.getId(), new ItemDto("ДРЕль", "очень мощная", true));
		ItemDto itemDto5 = itemService.put(userDto.getId(), new ItemDto("дрель", "ДРЕЛЬ самая классная", true));

		List<ItemDto> itemsDto= itemService.foundAvailableItemWithNameOrDescription("дрель");
		assertEquals(3, itemsDto.size());
		assertTrue(itemsDto.contains(itemDto1));
		assertFalse(itemsDto.contains(itemDto2));
		assertFalse(itemsDto.contains(itemDto3));
		assertTrue(itemsDto.contains(itemDto4));
		assertTrue(itemsDto.contains(itemDto5));

	}

}
