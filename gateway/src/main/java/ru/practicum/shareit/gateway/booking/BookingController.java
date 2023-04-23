package ru.practicum.shareit.gateway.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.booking.dto.CreateBookingRequestDto;
import ru.practicum.shareit.gateway.booking.dto.State;
import ru.practicum.shareit.gateway.header.HeaderConst;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(HeaderConst.USER_ID_HEADER) Integer userId,
                                         @RequestBody @Valid CreateBookingRequestDto bookingDto) {
        log.info("Получен запрос на создание бронирования от пользователя с id = {}", userId);
        return bookingClient.bookItem(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateStatus(@RequestHeader(HeaderConst.USER_ID_HEADER) Integer ownerId,
                                               @PathVariable Integer bookingId,
                                               @RequestParam Boolean approved) {

        log.info("Получен запрос на обновление статуса от букинга с id = {}", bookingId);
        return bookingClient.updateStatus(bookingId, approved, ownerId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(
            @RequestHeader(HeaderConst.USER_ID_HEADER) Integer userId,
            @PathVariable Integer bookingId) {
        log.info("Получен запрос букинга id = {}", bookingId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnersAllBookingsViaStatus(
            @RequestHeader(HeaderConst.USER_ID_HEADER) Integer ownerId,
            @RequestParam(name = "state", required = false, defaultValue = "ALL") String stringState,
            @RequestParam(name = "from", required = false) Integer from,
            @RequestParam(name = "size", required = false) Integer size) {

        log.info("Получен запрос всех букингов владельца с  id = {}", ownerId);
        return bookingClient.getOwnerBookings(ownerId, State.fromValue(stringState), from, size);
    }

    @GetMapping
    public ResponseEntity<Object> getBookersAllBooking(
            @RequestHeader(HeaderConst.USER_ID_HEADER) Integer bookerId,
            @RequestParam(name = "state", required = false, defaultValue = "ALL") String stringState,
            @RequestParam(name = "from", required = false) Integer from,
            @RequestParam(name = "size", required = false) Integer size) {
        log.info("Получен запрос всех букингов пользователя с  id = {}", bookerId);
        return bookingClient.getBookings(bookerId, State.fromValue(stringState), from, size);
    }
}
