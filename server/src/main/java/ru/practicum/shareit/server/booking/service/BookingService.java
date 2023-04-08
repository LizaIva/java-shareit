package ru.practicum.shareit.server.booking.service;

import ru.practicum.shareit.server.booking.dto.BookingDto;
import ru.practicum.shareit.server.booking.dto.CreateBookingRequestDto;
import ru.practicum.shareit.server.booking.dto.State;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    BookingDto put(CreateBookingRequestDto bookingDto, Integer bookerId);

    BookingDto updateStatus(int bookingId, boolean status, int ownerId);

    BookingDto getBookingById(int bookingId, int userId);

    List<BookingDto> getOwnersAllBookingsViaStatus(int ownerId, State state, Integer size, Integer from);

    List<BookingDto> getBookersAllBooking(int bookerId, State state, Integer size, Integer from);

    void checkTimeForBooking(LocalDateTime start, LocalDateTime end, Integer itemId);
}
