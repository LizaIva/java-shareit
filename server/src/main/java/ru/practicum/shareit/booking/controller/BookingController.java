package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingRequestDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

import static ru.practicum.shareit.booking.dto.State.fromValue;
import static ru.practicum.shareit.header.HeaderConst.USER_ID_HEADER;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping()
    public BookingDto create(@RequestHeader(USER_ID_HEADER) Integer userId,
                             @RequestBody CreateBookingRequestDto bookingDto) {
        log.info("Получен запрос на создание бронирования от пользователя с id = {}", userId);
        return bookingService.put(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto updateStatus(@RequestHeader(USER_ID_HEADER) Integer ownerId,
                                   @PathVariable Integer bookingId,
                                   @RequestParam Boolean approved) {

        log.info("Получен запрос на обновление статуса от букинга с id = {}", bookingId);
        return bookingService.updateStatus(bookingId, approved, ownerId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(
            @RequestHeader(USER_ID_HEADER) Integer userId,
            @PathVariable Integer bookingId) {
        log.info("Получен запрос букинга id = {}", bookingId);
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping("/owner")
    public List<BookingDto> getOwnersAllBookingsViaStatus(
            @RequestHeader(USER_ID_HEADER) Integer ownerId,
            @RequestParam(name = "state", required = false, defaultValue = "ALL") String stringState,
            @RequestParam(name = "from", required = false) Integer from,
            @RequestParam(name = "size", required = false) Integer size) {

        log.info("Получен запрос всех букингов владельца с  id = {}", ownerId);
        return bookingService.getOwnersAllBookingsViaStatus(ownerId, fromValue(stringState), size, from);
    }

    @GetMapping
    public List<BookingDto> getBookersAllBooking(
            @RequestHeader(USER_ID_HEADER) Integer bookerId,
            @RequestParam(name = "state", required = false, defaultValue = "ALL") String stringState,
            @RequestParam(name = "from", required = false) Integer from,
            @RequestParam(name = "size", required = false) Integer size) {
        log.info("Получен запрос всех букингов пользователя с  id = {}", bookerId);
        return bookingService.getBookersAllBooking(bookerId, fromValue(stringState), size, from);
    }
}

