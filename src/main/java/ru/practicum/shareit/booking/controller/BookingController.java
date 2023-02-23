package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.UpdateBookingStatusDto;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

import static ru.practicum.shareit.header.HeaderConst.USER_ID_HEADER;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/{userId}")
    public BookingDto create(@PathVariable Integer userId,
                             @RequestBody @Valid BookingDto bookingDto) {
        log.info("Получен запрос на создание бронирования от пользователя с id = {}", userId);
        return bookingService.put(bookingDto, userId);
    }

    @PatchMapping("/status/change")
    public BookingDto updateStatus(@RequestHeader(USER_ID_HEADER) Integer ownerId,
                                   @RequestBody @Valid UpdateBookingStatusDto bookingDto) {

        log.info("Получен запрос на обновление статуса от букинга с id = {}", bookingDto.getId());
        return bookingService.updateStatus(bookingDto, ownerId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@PathVariable Integer bookingId) {
        log.info("Получен запрос букинга id = {}", bookingId);
        return bookingService.getBookingById(bookingId);
    }

    @GetMapping("/owners")
    public List<BookingDto> getOwnersAllBookingsViaStatus(
            @RequestHeader(USER_ID_HEADER) Integer ownerId,
            @RequestBody Status status) {
        log.info("Получен запрос всех букингов владельца с  id = {}", ownerId);
        return bookingService.getOwnersAllBookingsViaStatus(ownerId, status);
    }

    @GetMapping("/bookers/{bookerId}")
    public List<BookingDto> getBookersAllBooking(@PathVariable Integer bookerId) {
        log.info("Получен запрос всех букингов пользователя с  id = {}", bookerId);
        return bookingService.getBookersAllBooking(bookerId);
    }

    @GetMapping("/items/{itemId}")
    public List<BookingDto> getItemsAllBooking(@PathVariable Integer itemId) {
        log.info("Получен запрос всех букингов предмета с  id = {}", itemId);
        return bookingService.getItemsAllBooking(itemId);
    }
}

