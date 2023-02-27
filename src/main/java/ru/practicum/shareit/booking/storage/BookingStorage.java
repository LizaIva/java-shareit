package ru.practicum.shareit.booking.storage;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.util.List;

public interface BookingStorage {
    Booking put(Booking booking, Integer bookerId);

    Booking getBookingById(int bookingId);

    Booking updateStatus(int bookingId, Status status, int ownerId);

    List<Booking> getOwnersAllBookingsViaStatus(int ownerId, Status status);

    List<Booking> getBookersAllBooking(int bookerId);

    List<Booking> getItemsAllBooking(int itemId);

    void checkBooking(int id);
}
