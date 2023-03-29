package ru.practicum.shareit.booking.storage.impl;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingRequestDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.CheckOwnerException;
import ru.practicum.shareit.exception.UnknownDataException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class BookingDbStorageImplTest {
    private final UserService userService;
    private final ItemService itemService;

    private final BookingService bookingService;

    @Test
    void putAndPutUnknownUserAndPutOwnerItemToBooking() {
        UserDto ownerDto = UserDto.builder()
                .id(1)
                .name("Liza")
                .email("iva@mail.ru")
                .build();
        userService.put(ownerDto);

        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("Молоток")
                .description("Для гвоздей")
                .available(true)
                .comments(Collections.emptyList())
                .build();
        itemService.put(ownerDto.getId(), itemDto);

        UserDto bookerDto = UserDto.builder()
                .id(2)
                .name("Masha")
                .email("abc@mail.ru")
                .build();
        userService.put(bookerDto);

        BookingDto bookingDto = BookingDto.builder()
                .id(1)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(3))
                .status(Status.WAITING)
                .item(itemDto)
                .itemId(itemDto.getId())
                .booker(bookerDto)
                .bookerId(bookerDto.getId())
                .build();

        CreateBookingRequestDto createBookingRequestDto = CreateBookingRequestDto.builder()
                .id(1)
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .status(Status.WAITING)
                .itemId(1)
                .build();

        BookingDto actualBooking = bookingService.put(createBookingRequestDto, bookerDto.getId());

        assertEquals(bookingDto, actualBooking);

        assertThrows(UnknownDataException.class, () -> bookingService.put(createBookingRequestDto, 99));


        assertThrows(UnknownDataException.class, () -> bookingService.put(createBookingRequestDto, ownerDto.getId()));


        ItemDto itemDto2 = ItemDto.builder()
                .id(2)
                .name("Коньки")
                .description("Детские")
                .available(false)
                .comments(Collections.emptyList())
                .build();
        itemService.put(ownerDto.getId(), itemDto);

        BookingDto bookingDto2 = BookingDto.builder()
                .id(2)
                .start(LocalDateTime.now().plusDays(9))
                .end(LocalDateTime.now().plusDays(12))
                .status(Status.WAITING)
                .item(itemDto2)
                .itemId(itemDto2.getId())
                .booker(bookerDto)
                .bookerId(bookerDto.getId())
                .build();

        CreateBookingRequestDto createBookingRequestDto2 = CreateBookingRequestDto.builder()
                .id(2)
                .start(bookingDto2.getStart())
                .end(bookingDto2.getEnd())
                .status(Status.WAITING)
                .itemId(2)
                .build();

        BookingDto bookingDto3 = BookingDto.builder()
                .id(3)
                .start(LocalDateTime.now().minusDays(9))
                .end(LocalDateTime.now().minusDays(10))
                .status(Status.WAITING)
                .item(itemDto)
                .itemId(itemDto.getId())
                .booker(bookerDto)
                .bookerId(bookerDto.getId())
                .build();

        CreateBookingRequestDto createBookingRequestDto3 = CreateBookingRequestDto.builder()
                .id(3)
                .start(bookingDto3.getStart())
                .end(bookingDto3.getEnd())
                .status(Status.WAITING)
                .itemId(1)
                .build();

        assertThrows(UnknownDataException.class, () ->
                bookingService.put(createBookingRequestDto2, bookerDto.getId()));

        assertThrows(ValidationException.class, () ->
                bookingService.put(createBookingRequestDto3, bookerDto.getId()));
    }

    @Test
    void getBookingByIdAndGetUnknownBooking() {
        UserDto ownerDto = UserDto.builder()
                .id(1)
                .name("Liza")
                .email("iva@mail.ru")
                .build();
        userService.put(ownerDto);

        UserDto userDto = UserDto.builder()
                .id(6)
                .name("Liza")
                .email("klp@mail.ru")
                .build();
        userService.put(userDto);

        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("Молоток")
                .description("Для гвоздей")
                .available(true)
                .comments(Collections.emptyList())
                .build();
        itemService.put(ownerDto.getId(), itemDto);

        UserDto bookerDto1 = UserDto.builder()
                .id(2)
                .name("Masha")
                .email("abc@mail.ru")
                .build();
        userService.put(bookerDto1);

        UserDto bookerDto2 = UserDto.builder()
                .id(3)
                .name("Nikita")
                .email("qwe@mail.ru")
                .build();
        userService.put(bookerDto2);

        BookingDto bookingDto1 = BookingDto.builder()
                .id(1)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(3))
                .status(Status.WAITING)
                .item(itemDto)
                .itemId(itemDto.getId())
                .booker(bookerDto1)
                .bookerId(bookerDto1.getId())
                .build();

        CreateBookingRequestDto createBookingRequestDto1 = CreateBookingRequestDto.builder()
                .id(1)
                .start(bookingDto1.getStart())
                .end(bookingDto1.getEnd())
                .status(Status.WAITING)
                .itemId(1)
                .build();
        bookingService.put(createBookingRequestDto1, bookerDto1.getId());

        BookingDto bookingDto2 = BookingDto.builder()
                .id(2)
                .start(LocalDateTime.now().plusDays(7))
                .end(LocalDateTime.now().plusDays(9))
                .status(Status.WAITING)
                .item(itemDto)
                .itemId(itemDto.getId())
                .booker(bookerDto2)
                .bookerId(bookerDto2.getId())
                .build();

        CreateBookingRequestDto createBookingRequestDto2 = CreateBookingRequestDto.builder()
                .id(2)
                .start(bookingDto2.getStart())
                .end(bookingDto2.getEnd())
                .status(Status.WAITING)
                .itemId(1)
                .build();
        bookingService.put(createBookingRequestDto2, bookerDto2.getId());

        BookingDto actualFromOwner = bookingService.getBookingById(createBookingRequestDto1.getId(), ownerDto.getId());
        assertEquals(bookingDto1, actualFromOwner);

        BookingDto actualFromBooker = bookingService.getBookingById(createBookingRequestDto1.getId(), bookerDto1.getId());
        assertEquals(bookingDto1, actualFromBooker);

        assertThrows(UnknownDataException.class, () ->
                bookingService.getBookingById(bookingDto1.getId(), userDto.getId()));

        assertThrows(UnknownDataException.class, () ->
                bookingService.getBookingById(bookingDto1.getId(), 99));

        assertThrows(UnknownDataException.class, () ->
                bookingService.getBookingById(99, bookerDto2.getId()));
    }

    @Test
    void updateStatusAndUpdateFromNotOwner() {
        UserDto ownerDto = UserDto.builder()
                .id(1)
                .name("Liza")
                .email("iva@mail.ru")
                .build();
        userService.put(ownerDto);

        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("Молоток")
                .description("Для гвоздей")
                .available(true)
                .comments(Collections.emptyList())
                .build();
        itemService.put(ownerDto.getId(), itemDto);

        UserDto bookerDto = UserDto.builder()
                .id(2)
                .name("Masha")
                .email("abc@mail.ru")
                .build();
        userService.put(bookerDto);

        BookingDto bookingDto = BookingDto.builder()
                .id(1)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(3))
                .status(Status.WAITING)
                .item(itemDto)
                .itemId(itemDto.getId())
                .booker(bookerDto)
                .bookerId(bookerDto.getId())
                .build();

        CreateBookingRequestDto createBookingRequestDto = CreateBookingRequestDto.builder()
                .id(1)
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .status(Status.WAITING)
                .itemId(1)
                .build();
        bookingService.put(createBookingRequestDto, bookerDto.getId());

        bookingService.updateStatus(bookingDto.getId(), true, ownerDto.getId());

        BookingDto actualBooking = bookingService.getBookingById(bookingDto.getId(), ownerDto.getId());
        assertEquals(Status.APPROVED, actualBooking.getStatus());

        assertThrows(CheckOwnerException.class, () ->
                bookingService.updateStatus(bookingDto.getId(), false, bookerDto.getId()));

        assertThrows(UnknownDataException.class, () ->
                bookingService.updateStatus(99, false, ownerDto.getId()));
    }

    @Test
    void getOwnersAllBookings() {
        UserDto ownerDto = UserDto.builder()
                .id(1)
                .name("Liza")
                .email("iva@mail.ru")
                .build();
        userService.put(ownerDto);

        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("Молоток")
                .description("Для гвоздей")
                .available(true)
                .comments(Collections.emptyList())
                .build();
        itemService.put(ownerDto.getId(), itemDto);

        UserDto bookerDto1 = UserDto.builder()
                .id(2)
                .name("Masha")
                .email("abc@mail.ru")
                .build();
        userService.put(bookerDto1);

        UserDto bookerDto2 = UserDto.builder()
                .id(3)
                .name("Nikita")
                .email("qwe@mail.ru")
                .build();
        userService.put(bookerDto2);

        BookingDto bookingDto1 = BookingDto.builder()
                .id(1)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(3))
                .status(Status.WAITING)
                .item(itemDto)
                .itemId(itemDto.getId())
                .booker(bookerDto1)
                .bookerId(bookerDto1.getId())
                .build();

        CreateBookingRequestDto createBookingRequestDto1 = CreateBookingRequestDto.builder()
                .id(1)
                .start(bookingDto1.getStart())
                .end(bookingDto1.getEnd())
                .status(Status.WAITING)
                .itemId(1)
                .build();
        bookingService.put(createBookingRequestDto1, bookerDto1.getId());

        BookingDto bookingDto2 = BookingDto.builder()
                .id(2)
                .start(LocalDateTime.now().plusDays(7))
                .end(LocalDateTime.now().plusDays(9))
                .status(Status.WAITING)
                .item(itemDto)
                .itemId(itemDto.getId())
                .booker(bookerDto2)
                .bookerId(bookerDto2.getId())
                .build();

        CreateBookingRequestDto createBookingRequestDto2 = CreateBookingRequestDto.builder()
                .id(2)
                .start(bookingDto2.getStart())
                .end(bookingDto2.getEnd())
                .status(Status.WAITING)
                .itemId(1)
                .build();
        bookingService.put(createBookingRequestDto2, bookerDto2.getId());
        bookingService.updateStatus(bookingDto2.getId(), false, ownerDto.getId());
        BookingDto bookingDto2AfterUpdate = bookingService.getBookingById(bookingDto2.getId(), ownerDto.getId());

        UserDto ownerDto2 = UserDto.builder()
                .id(4)
                .name("Nikita")
                .email("klp@mail.ru")
                .build();
        userService.put(ownerDto2);

        ItemDto itemDto2 = ItemDto.builder()
                .id(2)
                .name("Конькт")
                .description("Детские")
                .available(true)
                .comments(Collections.emptyList())
                .build();
        itemService.put(ownerDto2.getId(), itemDto2);

        UserDto bookerDto3 = UserDto.builder()
                .id(5)
                .name("Nikita")
                .email("mhbjk@mail.ru")
                .build();
        userService.put(bookerDto3);

        BookingDto bookingDto3 = BookingDto.builder()
                .id(3)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(3))
                .status(Status.WAITING)
                .item(itemDto)
                .itemId(itemDto.getId())
                .booker(bookerDto3)
                .bookerId(bookerDto3.getId())
                .build();

        CreateBookingRequestDto createBookingRequestDto3 = CreateBookingRequestDto.builder()
                .id(3)
                .start(bookingDto1.getStart())
                .end(bookingDto1.getEnd())
                .status(Status.WAITING)
                .itemId(2)
                .build();
        bookingService.put(createBookingRequestDto3, bookerDto3.getId());

        List<BookingDto> owners1Bookings = List.of(bookingDto2AfterUpdate, bookingDto1);

        List<BookingDto> actualBookingsOwner1 =
                bookingService.getOwnersAllBookingsViaStatus(ownerDto.getId(), State.ALL, null, null);

        assertEquals(owners1Bookings, actualBookingsOwner1);

        List<BookingDto> owners1BookingsWithStateRejected = List.of(bookingDto2AfterUpdate);

        List<BookingDto> actualBookingsOwnerWithStateRejected =
                bookingService.getOwnersAllBookingsViaStatus(ownerDto.getId(), State.REJECTED, null, null);

        assertEquals(owners1BookingsWithStateRejected, actualBookingsOwnerWithStateRejected);
    }

    @Test
    void getBookersAllBooking() {
        UserDto ownerDto = UserDto.builder()
                .id(1)
                .name("Liza")
                .email("iva@mail.ru")
                .build();
        userService.put(ownerDto);

        ItemDto itemDto = ItemDto.builder()
                .id(1)
                .name("Молоток")
                .description("Для гвоздей")
                .available(true)
                .comments(Collections.emptyList())
                .build();
        itemService.put(ownerDto.getId(), itemDto);

        UserDto bookerDto1 = UserDto.builder()
                .id(2)
                .name("Masha")
                .email("abc@mail.ru")
                .build();
        userService.put(bookerDto1);


        BookingDto bookingDto1 = BookingDto.builder()
                .id(1)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(3))
                .status(Status.WAITING)
                .item(itemDto)
                .itemId(itemDto.getId())
                .booker(bookerDto1)
                .bookerId(bookerDto1.getId())
                .build();

        CreateBookingRequestDto createBookingRequestDto1 = CreateBookingRequestDto.builder()
                .id(1)
                .start(bookingDto1.getStart())
                .end(bookingDto1.getEnd())
                .status(Status.WAITING)
                .itemId(1)
                .build();
        bookingService.put(createBookingRequestDto1, bookerDto1.getId());

        BookingDto bookingDto2 = BookingDto.builder()
                .id(2)
                .start(LocalDateTime.now().plusDays(7))
                .end(LocalDateTime.now().plusDays(9))
                .status(Status.WAITING)
                .item(itemDto)
                .itemId(itemDto.getId())
                .booker(bookerDto1)
                .bookerId(bookerDto1.getId())
                .build();

        CreateBookingRequestDto createBookingRequestDto2 = CreateBookingRequestDto.builder()
                .id(2)
                .start(bookingDto2.getStart())
                .end(bookingDto2.getEnd())
                .status(Status.WAITING)
                .itemId(1)
                .build();
        bookingService.put(createBookingRequestDto2, bookerDto1.getId());
        bookingService.updateStatus(bookingDto2.getId(), false, ownerDto.getId());
        BookingDto bookingDto2AfterUpdate = bookingService.getBookingById(bookingDto2.getId(), ownerDto.getId());

        UserDto ownerDto2 = UserDto.builder()
                .id(3)
                .name("Nikita")
                .email("klp@mail.ru")
                .build();
        userService.put(ownerDto2);

        ItemDto itemDto2 = ItemDto.builder()
                .id(2)
                .name("Коньки")
                .description("Детские")
                .available(true)
                .comments(Collections.emptyList())
                .build();
        itemService.put(ownerDto2.getId(), itemDto2);

        BookingDto bookingDto3 = BookingDto.builder()
                .id(3)
                .start(LocalDateTime.now().plusDays(1))
                .end(LocalDateTime.now().plusDays(3))
                .status(Status.WAITING)
                .item(itemDto2)
                .itemId(itemDto2.getId())
                .booker(bookerDto1)
                .bookerId(bookerDto1.getId())
                .build();

        CreateBookingRequestDto createBookingRequestDto3 = CreateBookingRequestDto.builder()
                .id(3)
                .start(bookingDto3.getStart())
                .end(bookingDto3.getEnd())
                .status(Status.WAITING)
                .itemId(2)
                .build();
        bookingService.put(createBookingRequestDto3, bookerDto1.getId());

        List<BookingDto> bookingsForBooker1 = List.of(bookingDto2AfterUpdate, bookingDto3, bookingDto1);
        List<BookingDto> actualBookingsForBooker1 = bookingService.getBookersAllBooking(bookerDto1.getId(), State.ALL, null, null);

        assertEquals(bookingsForBooker1, actualBookingsForBooker1);

        List<BookingDto> bookingsForBooker1WithState = List.of(bookingDto1, bookingDto3);
        List<BookingDto> actualBookingsForBooker1WithState = bookingService.getBookersAllBooking(bookerDto1.getId(), State.WAITING, null, null);

        assertEquals(bookingsForBooker1WithState, actualBookingsForBooker1WithState);
    }
}