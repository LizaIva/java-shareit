package ru.practicum.shareit.booking.storage;

import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.util.List;

public interface BookingStorage {

    String NOT_FOUND_BOOKING_MSG = "Booking с id = %s не найден.";
    String USER_NOT_OWNER_OF_THIS_BOOKING_MSG = "Owner с id = %s не может изменить статус чужого booking";

    Booking put(Booking booking, Integer bookerId);

    Booking getBookingById(int bookingId, int userId);

    Booking updateStatus(int bookingId, Status status, int ownerId);

    List<Booking> getOwnersAllBookings(int ownerId, Integer size, Integer from);

    List<Booking> getBookersAllBooking(int bookerId, Integer size, Integer from);

    List<Booking> getItemsAllBooking(int itemId);

    void checkBooking(int id);

    void checkUserItemOwnerByBookingId(int bookingId, int userId);
}
