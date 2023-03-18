package ru.practicum.shareit.booking.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.CheckOwnerException;
import ru.practicum.shareit.exception.UnknownDataException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.Optional;

@Component("bookingDbStorageImpl")
@RequiredArgsConstructor
public class BookingDbStorageImpl implements BookingStorage {
    private final BookingRepository bookingRepository;
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    @Override
    public Booking put(Booking booking, Integer bookerId) {
        checkItemIsAvailable(booking.getBookedItem().getId());

        User booker = userStorage.getUserById(bookerId);
        booking.setBooker(booker);
        return bookingRepository.saveAndFlush(booking);
    }


    @Override
    public Booking getBookingById(int bookingId, int userId) {
        return Optional.ofNullable(bookingRepository.findByBookingIdAndIfUserOwnerOrBooker(bookingId, userId))
                .orElseThrow(() -> new UnknownDataException(String.format(NOT_FOUND_BOOKING_MSG, bookingId)));
    }

    @Override
    public Booking updateStatus(int bookingId, Status status, int ownerId) {
        Booking booking = getBookingById(bookingId, ownerId);

        booking.setStatus(status);
        bookingRepository.saveAndFlush(booking);

        return booking;
    }

    @Override
    public List<Booking> getOwnersAllBookings(int ownerId) {
        return bookingRepository.getBookingsByOwnerId(ownerId);
    }

    @Override
    public List<Booking> getBookersAllBooking(int bookerId) {
        userStorage.checkUser(bookerId);
        return bookingRepository.getBookingsByStatus(bookerId);
    }

    @Override
    public List<Booking> getItemsAllBooking(int itemId) {
        return null;
    }

    @Override
    public void checkBooking(int id) {
        if (!bookingRepository.existsById(id)) {
            throw new UnknownDataException((String.format(NOT_FOUND_BOOKING_MSG, id)));
        }
    }

    @Override
    public void checkUserItemOwnerByBookingId(int bookingId, int userId) {
        if (!bookingRepository.isUserItemOwnerByBookingId(bookingId, userId)) {
            throw new CheckOwnerException((String.format(USER_NOT_OWNER_OF_THIS_BOOKING_MSG, userId)));
        }
    }

    private void checkItemIsAvailable(int itemId) {
        Item item = itemStorage.getItemById(itemId);
        if (!item.getAvailable()) {
            throw new ValidationException("Невозможно забронировать занятый предмет");
        }
    }
}

