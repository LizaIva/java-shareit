package ru.practicum.shareit;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.UpdateBookingStatusDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.CheckOwnerException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class BookingServiceTest {
    private final ItemService itemService;
    private final UserService userService;
    private final BookingService bookingService;

    @Test
    @DisplayName("Создание букинга")
    void createBookingTest(){
        UserDto ownerDto = userService.put(new UserDto("Liza", "iva-iva@mail.ru"));
        ItemDto itemDto = itemService.put(ownerDto.getId(), new ItemDto("дрель", "очень мощная", true));
        UserDto userDto = userService.put(new UserDto("Nikita", "ya@bk.ru"));

        BookingDto bookingDto = bookingService.put(new BookingDto(LocalDateTime.of(2034, 10, 14, 8, 23),
                LocalDateTime.of(2034, 11, 14, 8, 23), itemDto.getId()), userDto.getId());

        assertEquals(bookingDto, bookingService.getBookingById(bookingDto.getId()), "не верно получены данные о букинге");
        assertEquals(List.of(bookingDto), bookingService.getBookersAllBooking(userDto.getId()), "неверное количество букингов в базе");
        assertEquals(List.of(bookingDto), bookingService.getItemsAllBooking(itemDto.getId()), "неверное количество букингов в базе");
        assertEquals(List.of(bookingDto), bookingService.getOwnersAllBookingsViaStatus(ownerDto.getId(), null), "неверное количество букингов в базе");

    }

    @Test
    @DisplayName("Изменение статуса букинга")
    void updateStatusTest(){
        UserDto ownerDto = userService.put(new UserDto("Liza", "iva-iva@mail.ru"));
        ItemDto itemDto = itemService.put(ownerDto.getId(), new ItemDto("дрель", "очень мощная", true));
        UserDto userDto = userService.put(new UserDto("Nikita", "ya@bk.ru"));

        BookingDto bookingDto = bookingService.put(new BookingDto(LocalDateTime.of(2034, 10, 14, 8, 23),
                LocalDateTime.of(2034, 11, 14, 8, 23), itemDto.getId()), userDto.getId());

        UpdateBookingStatusDto updatedBookingStatusDto = new UpdateBookingStatusDto(bookingDto.getId(), Status.APPROVED, itemDto.getId());
        BookingDto updatedBookingDto = bookingService.updateStatus(updatedBookingStatusDto, ownerDto.getId());

        assertEquals(Status.APPROVED, bookingService.getBookingById(bookingDto.getId()).getStatus(), "Не произошло изменение статуса букинга");
    }

    @Test
    @DisplayName("Изменение статуса букинга НЕ владельцем предмета")
    void updateStatusByNotOwnerTest(){
        UserDto ownerDto = userService.put(new UserDto("Liza", "iva-iva@mail.ru"));
        ItemDto itemDto = itemService.put(ownerDto.getId(), new ItemDto("дрель", "очень мощная", true));
        UserDto userDto = userService.put(new UserDto("Nikita", "ya@bk.ru"));

        BookingDto bookingDto = bookingService.put(new BookingDto(LocalDateTime.of(2034, 10, 14, 8, 23),
                LocalDateTime.of(2034, 11, 14, 8, 23), itemDto.getId()), userDto.getId());

        UpdateBookingStatusDto updatedBookingStatusDto = new UpdateBookingStatusDto(bookingDto.getId(), Status.APPROVED, itemDto.getId());
        assertThrows(CheckOwnerException.class, () ->
                bookingService.updateStatus(updatedBookingStatusDto, userDto.getId()));
    }

    @Test
    @DisplayName("Создание букинга на занятое время")
    void createBookingForUnavailableTime(){
        UserDto ownerDto = userService.put(new UserDto("Liza", "iva-iva@mail.ru"));
        ItemDto itemDto = itemService.put(ownerDto.getId(), new ItemDto("дрель", "очень мощная", true));
        UserDto userDto = userService.put(new UserDto("Nikita", "ya@bk.ru"));

        BookingDto bookingDto = bookingService.put(new BookingDto(LocalDateTime.of(2034, 10, 14, 8, 23),
                LocalDateTime.of(2034, 11, 14, 8, 23), itemDto.getId()), userDto.getId());

        assertThrows(ValidationException.class, () -> bookingService.put(new BookingDto(LocalDateTime.of(2034, 10, 14, 8, 23),
                LocalDateTime.of(2034, 11, 14, 8, 23), itemDto.getId()), userDto.getId()));

        assertThrows(ValidationException.class, () -> bookingService.put(new BookingDto(LocalDateTime.of(2033, 10, 14, 8, 23),
                LocalDateTime.of(2034, 11, 14, 8, 23), itemDto.getId()), userDto.getId()));

        assertThrows(ValidationException.class, () -> bookingService.put(new BookingDto(LocalDateTime.of(2034, 12, 14, 8, 23),
                LocalDateTime.of(2034, 11, 14, 8, 23), itemDto.getId()), userDto.getId()));

        assertThrows(ValidationException.class, () -> bookingService.put(new BookingDto(LocalDateTime.of(2032, 10, 14, 8, 23),
                LocalDateTime.of(2035, 11, 14, 8, 23), itemDto.getId()), userDto.getId()));

        assertThrows(ValidationException.class, () -> bookingService.put(new BookingDto(LocalDateTime.of(2034, 10, 16, 8, 23),
                LocalDateTime.of(2034, 11, 9, 8, 23), itemDto.getId()), userDto.getId()));
    }

}
