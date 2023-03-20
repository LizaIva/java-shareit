package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingRequestDto;
import ru.practicum.shareit.booking.dto.State;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    BookingDto put(CreateBookingRequestDto bookingDto, Integer bookerId);

    BookingDto updateStatus(int bookingId, boolean status, int ownerId);

    BookingDto getBookingById(int bookingId, int userId);

    List<BookingDto> getOwnersAllBookingsViaStatus(int ownerId, State state);

    List<BookingDto> getBookersAllBooking(int ownerId, State state);

    List<BookingDto> getItemsAllBooking(int itemId);

    void checkTimeForBooking(LocalDateTime start, LocalDateTime end, Integer itemId);
}
