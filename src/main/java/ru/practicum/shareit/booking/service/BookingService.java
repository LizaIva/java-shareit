package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.UpdateBookingStatusDto;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingService {
    BookingDto put(BookingDto bookingDto, Integer bookerId);

    BookingDto updateStatus(UpdateBookingStatusDto bookingDto, int ownerId);

    BookingDto getBookingById(int bookingId);

    List<BookingDto> getOwnersAllBookingsViaStatus(int ownerId, Status status);

    List<BookingDto> getBookersAllBooking(int bookerId);

    List<BookingDto> getItemsAllBooking(int itemId);

    void checkTimeForBooking(LocalDateTime start, LocalDateTime end, Integer itemId);
}
