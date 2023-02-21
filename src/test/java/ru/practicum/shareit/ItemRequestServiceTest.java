package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ItemRequestServiceTest {
    private final ItemRequestService itemRequestService;
    private final UserService userService;

    @Test
    @DisplayName("Создание запроса")
    public void putAndGetRequestTest() {
        UserDto userDto = userService.put(new UserDto("Liza", "iva-iva@mail.ru"));
        ItemRequestDto itemRequestDto = itemRequestService.put(new ItemRequestDto("Срочно нужна норвковая шуба", userDto.getId()));
        assertEquals(itemRequestDto, itemRequestService.get(itemRequestDto.getId()), "не верно получены данные о запросе");
    }

    @Test
    @DisplayName("Удаление запроса")
    public void deleteRequestTest() {
        UserDto userDto = userService.put(new UserDto("Liza", "iva-iva@mail.ru"));
        ItemRequestDto itemRequestDto = itemRequestService.put(new ItemRequestDto("Срочно нужна норвковая шуба", userDto.getId()));
        itemRequestService.delete(itemRequestDto.getId());
        List<ItemRequestDto> itemRequestDtoList = itemRequestService.getAll();
        assertEquals(0, itemRequestDtoList.size(), "Не произошло удаление запроса");

    }
}
