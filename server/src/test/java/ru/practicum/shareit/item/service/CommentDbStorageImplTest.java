package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.CreateBookingRequestDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.CheckBookerException;
import ru.practicum.shareit.exception.UnknownDataException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class CommentDbStorageImplTest {

    private final ItemService itemService;
    private final CommentService commentService;
    private final UserService userService;

    private final BookingService bookingService;

    LocalDateTime now = LocalDateTime.now();

    @Test
    void putAndPutWithUnknownItemAndWithUnknownUserAndWithUnknownBooking() {
        UserDto ownerDto = UserDto.builder()
                .id(1)
                .name("Liza")
                .email("iva@mail.ru")
                .build();

        userService.put(ownerDto);

        UserDto bookerDto = UserDto.builder()
                .id(2)
                .name("Masha")
                .email("abc@mail.ru")
                .build();

        userService.put(bookerDto);

        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("Молоток")
                .description("Для гвоздей")
                .available(true)
                .comments(null)
                .build();
        itemService.put(ownerDto.getId(), itemDto);

        ItemDto itemDto2 = ItemDto.builder()
                .id(2)
                .name("Отвертка")
                .description("Строительная")
                .available(true)
                .comments(null)
                .build();
        itemService.put(ownerDto.getId(), itemDto2);

        CreateBookingRequestDto bookingDto = CreateBookingRequestDto.builder()
                .id(1)
                .start(now.minusDays(7))
                .end(now.minusDays(5))
                .itemId(itemDto.getId())
                .status(Status.APPROVED)
                .build();

        bookingService.put(bookingDto, bookerDto.getId());

        CreateCommentDto createCommentDto = CreateCommentDto.builder()
                .text("Отличный товар")
                .build();

        CommentDto actual = commentService.put(itemDto.getId(), bookerDto.getId(), createCommentDto);

        assertEquals(createCommentDto.getText(), actual.getText());

        assertThrows(UnknownDataException.class, () -> commentService.put(99, bookerDto.getId(), createCommentDto));
        assertThrows(UnknownDataException.class, () -> commentService.put(itemDto.getId(), 99, createCommentDto));
        assertThrows(CheckBookerException.class, () -> commentService.put(itemDto2.getId(), bookerDto.getId(), createCommentDto));
    }

    @Test
    void get() {

    }

    @Test
    void checkComment() {
    }
}