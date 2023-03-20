//package ru.practicum.shareit.booking.storage.impl;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import ru.practicum.shareit.booking.model.Booking;
//import ru.practicum.shareit.booking.model.Status;
//import ru.practicum.shareit.booking.storage.BookingStorage;
//import ru.practicum.shareit.exception.UnknownDataException;
//import ru.practicum.shareit.item.storage.ItemStorage;
//import ru.practicum.shareit.user.storage.UserStorage;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class BookingStorageImpl {
//
//    private final UserStorage userStorage;
//    private final ItemStorage itemStorage;
//
//    private final HashMap<Integer, Booking> bookings = new HashMap<>();
//    private int counter = 0;
//
//    private final HashMap<Integer, List<Booking>> ownerIdToBookings = new HashMap<>();
//    private final HashMap<Integer, List<Booking>> bookerIdToBookings = new HashMap<>();
//    private final HashMap<Integer, List<Booking>> itemIdToBookings = new HashMap<>();
//
//    @Override
//    public Booking put(Booking booking, Integer bookerId) {
//        userStorage.checkUser(bookerId);
//        itemStorage.checkItem(booking.getBookedItem().getId());
//
//        if (booking.getId() == null) {
//            booking.setId(++counter);
//        }
//        booking.setStatus(Status.WAITING);
//        bookings.put(booking.getId(), booking);
//
//        int ownerId = itemStorage.getItemById(booking.getId()).getOwner().getId();
//
//        if (!ownerIdToBookings.containsKey(ownerId)) {
//            ownerIdToBookings.put(ownerId, new ArrayList<>(List.of(booking)));
//        } else {
//            List<Booking> ownerBookings = ownerIdToBookings.get(ownerId);
//            ownerBookings.add(booking);
//        }
//
//
//        if (!bookerIdToBookings.containsKey(bookerId)) {
//            bookerIdToBookings.put(bookerId, new ArrayList<>(List.of(booking)));
//        } else {
//            List<Booking> bookerBookings = bookerIdToBookings.get(bookerId);
//            bookerBookings.add(booking);
//        }
//
//        int bookingId = booking.getId();
//
//        if (!itemIdToBookings.containsKey(bookingId)) {
//            itemIdToBookings.put(bookingId, new ArrayList<>(List.of(booking)));
//        } else {
//            List<Booking> itemBookings = itemIdToBookings.get(bookingId);
//            itemBookings.add(booking);
//        }
//
//        return booking;
//    }
//
//    @Override
//    public Booking getBookingById(int bookingId) {
//        checkBooking(bookingId);
//        return bookings.get(bookingId);
//    }
//
//    @Override
//    public Booking updateStatus(int bookingId, Status status, int ownerId) {
//        userStorage.checkUser(ownerId);
//
//        Booking exsistBooking = bookings.get(bookingId);
//
//        if (exsistBooking == null) {
//            throw new UnknownDataException(BOOKING_NOT_EXIST_MSG);
//        }
//
//        exsistBooking.setStatus(status);
//        return exsistBooking;
//    }
//
//    @Override
//    public List<Booking> getOwnersAllBookingsViaStatus(int ownerId, Status status) {
//        userStorage.checkUser(ownerId);
//
//        if (ownerIdToBookings.isEmpty()) {
//            return null;
//        }
//
//        if (ownerIdToBookings.get(ownerId) == null) {
//            return null;
//        }
//
//        List<Booking> ownersBookings = ownerIdToBookings.get(ownerId);
//
//        if (status == null) {
//            return ownersBookings;
//        }
//
//        List<Booking> filteredViaStatus = new ArrayList<>();
//        for (Booking ownerBooking : ownersBookings) {
//            if (ownerBooking.getStatus() == status) {
//                filteredViaStatus.add(ownerBooking);
//            }
//        }
//        return filteredViaStatus;
//    }
//
//    @Override
//    public List<Booking> getBookersAllBooking(int bookerId) {
//        userStorage.checkUser(bookerId);
//
//        if (bookerIdToBookings.isEmpty()) {
//            return null;
//        }
//
//        if (bookerIdToBookings.get(bookerId) == null) {
//            return null;
//        }
//
//        return bookerIdToBookings.get(bookerId);
//    }
//
//    @Override
//    public List<Booking> getItemsAllBooking(int itemId) {
//        itemStorage.checkItem(itemId);
//
//        if (itemIdToBookings.isEmpty()) {
//            return null;
//        }
//
//        return itemIdToBookings.get(itemId);
//    }
//
//    @Override
//    public void checkBooking(int id) {
//        Booking booking = bookings.get(id);
//        if (booking == null) {
//            log.info("Booking с id = {} не найден.", id);
//            throw new UnknownDataException(String.format(NOT_FOUND_BOOKING_MSG, id));
//        }
//    }
//}
