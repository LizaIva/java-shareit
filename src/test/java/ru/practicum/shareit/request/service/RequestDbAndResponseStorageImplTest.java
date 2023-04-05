package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.UnknownDataException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.CreateRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
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
class RequestDbAndResponseStorageImplTest {
    private final RequestService requestService;
    private final ItemService itemService;
    private final UserService userService;

    @Test
    void putRequest() {
        UserDto userDto = UserDto.builder()
                .id(1)
                .name("Liza")
                .email("iva@mail.ru")
                .build();

        userService.put(userDto);

        CreateRequestDto createRequestDto = CreateRequestDto.builder()
                .description("Срочно нужны коньки")
                .build();


        RequestDto actualRequest = requestService.put(createRequestDto, userDto.getId());

        RequestDto expectedRequestDto = RequestDto.builder()
                .id(1)
                .responses(Collections.emptyList())
                .created(actualRequest.getCreated())
                .items(Collections.emptyList())
                .description("Срочно нужны коньки")
                .build();

        assertEquals(expectedRequestDto, actualRequest);

        assertThrows(UnknownDataException.class, () -> requestService.put(createRequestDto, 99));
    }

    @Test
    void getRequestById() {
        UserDto userDto = UserDto.builder()
                .id(1)
                .name("Liza")
                .email("iva@mail.ru")
                .build();

        userService.put(userDto);

        CreateRequestDto createRequestDto1 = CreateRequestDto.builder()
                .description("Срочно нужны коньки")
                .build();


        RequestDto actualRequest1 = requestService.put(createRequestDto1, userDto.getId());

        CreateRequestDto createRequestDto2 = CreateRequestDto.builder()
                .description("Нужно платье 18-го века")
                .build();


        RequestDto actualRequest2 = requestService.put(createRequestDto2, userDto.getId());

        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("дрель")
                .description("очень мощная")
                .available(true)
                .comments(Collections.emptyList())
                .requestId(actualRequest1.getId())
                .build();

        itemService.put(userDto.getId(), itemDto);

        RequestDto expectedRequestDto2 = RequestDto.builder()
                .id(2)
                .responses(Collections.emptyList())
                .created(actualRequest2.getCreated())
                .items(Collections.emptyList())
                .description("Нужно платье 18-го века")
                .build();

        RequestDto actualRequestDtoAfterGet = requestService.get(actualRequest2.getId(), userDto.getId());
        assertEquals(expectedRequestDto2, actualRequestDtoAfterGet);


        assertThrows(UnknownDataException.class, () -> requestService.get(actualRequest2.getId(), 99));
        assertThrows(UnknownDataException.class, () -> requestService.get(99, userDto.getId()));
    }

    @Test
    void getAll() {
        UserDto userDto = UserDto.builder()
                .id(1)
                .name("Liza")
                .email("iva@mail.ru")
                .build();

        UserDto userDto1 = userService.put(userDto);

        CreateRequestDto createRequestDto1 = CreateRequestDto.builder()
                .description("Срочно нужны коньки")
                .build();


        RequestDto actualRequest1 = requestService.put(createRequestDto1, userDto1.getId());

        RequestDto expectedRequestDto1 = RequestDto.builder()
                .id(1)
                .responses(Collections.emptyList())
                .created(actualRequest1.getCreated())
                .items(Collections.emptyList())
                .description("Срочно нужны коньки")
                .build();

        CreateRequestDto createRequestDto2 = CreateRequestDto.builder()
                .description("Нужно платье 18-го века")
                .build();


        RequestDto actualRequest2 = requestService.put(createRequestDto2, userDto1.getId());

        RequestDto expectedRequestDto2 = RequestDto.builder()
                .id(2)
                .responses(Collections.emptyList())
                .created(actualRequest2.getCreated())
                .items(Collections.emptyList())
                .description("Нужно платье 18-го века")
                .build();

        List<RequestDto> expectedRequestsDto = List.of(expectedRequestDto1, expectedRequestDto2);

        List<RequestDto> actualRequestsDto = requestService.getAll(userDto1.getId());
        assertEquals(expectedRequestsDto, actualRequestsDto);

        assertThrows(UnknownDataException.class, () -> requestService.getAll(99));
    }

    @Test
    void getAllWithOffset() {
        UserDto userDto = UserDto.builder()
                .id(1)
                .name("Liza")
                .email("iva@mail.ru")
                .build();

        userService.put(userDto);

        UserDto userDto2 = UserDto.builder()
                .id(2)
                .name("Masha")
                .email("abc@mail.ru")
                .build();

        userService.put(userDto);

        CreateRequestDto createRequestDto1 = CreateRequestDto.builder()
                .description("Срочно нужны коньки")
                .build();


        RequestDto actualRequest1 = requestService.put(createRequestDto1, userDto.getId());

        RequestDto expectedRequestDto1 = RequestDto.builder()
                .id(1)
                .responses(Collections.emptyList())
                .created(actualRequest1.getCreated())
                .items(Collections.emptyList())
                .description("Срочно нужны коньки")
                .build();

        CreateRequestDto createRequestDto2 = CreateRequestDto.builder()
                .description("Нужно платье 18-го века")
                .build();


        RequestDto actualRequest2 = requestService.put(createRequestDto2, userDto.getId());

        RequestDto expectedRequestDto2 = RequestDto.builder()
                .id(2)
                .responses(Collections.emptyList())
                .created(actualRequest2.getCreated())
                .items(Collections.emptyList())
                .description("Нужно платье 18-го века")
                .build();

        List<RequestDto> expectedRequestsDto = Collections.emptyList();

        List<RequestDto> actualRequestsDto = requestService.getAllWithOffset(5, 0, userDto.getId());
        assertEquals(expectedRequestsDto, actualRequestsDto);

        assertThrows(ValidationException.class, () -> requestService.getAllWithOffset(0, 0, userDto.getId()));
        assertThrows(ValidationException.class, () -> requestService.getAllWithOffset(-1, 0, userDto.getId()));

        List<RequestDto> expectedRequestsDto2 = List.of(expectedRequestDto1, expectedRequestDto2);
        List<RequestDto> actualRequestsDto2 = requestService.getAllWithOffset(5, 0, userDto2.getId());
        assertEquals(expectedRequestsDto2, actualRequestsDto2);

        List<RequestDto> expectedRequestsDto4 = List.of(expectedRequestDto1, expectedRequestDto2);
        List<RequestDto> actualRequestsDto4 = requestService.getAllWithOffset(5, 0, null);
        assertEquals(expectedRequestsDto2, actualRequestsDto2);

        List<RequestDto> expectedRequestsDto3 = List.of(expectedRequestDto1);
        List<RequestDto> actualRequestsDto3 = requestService.getAllWithOffset(1, 0, userDto2.getId());
        assertEquals(expectedRequestsDto3, actualRequestsDto3);

        assertThrows(UnknownDataException.class, () -> requestService.getAllWithOffset(null, null, 99));
    }
}