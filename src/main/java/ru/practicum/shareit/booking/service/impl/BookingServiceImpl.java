package ru.practicum.shareit.booking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.UpdateBookingStatusDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.booking.utils.BookingMapper;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final BookingStorage bookingStorage;
    private final BookingMapper bookingMapper;

    @Override
    public BookingDto put(BookingDto bookingDto, Integer bookerId) {
        userStorage.checkUser(bookerId);
        log.info("Создание бронирования");
        checkTimeForBooking(bookingDto.getStart(), bookingDto.getEnd(), bookingDto.getItemId());
        Booking booking = bookingMapper.mapToBooking(bookingDto);
        Booking savedBooking = bookingStorage.put(booking, bookerId);
        return bookingMapper.mapToBookingDto(savedBooking);
    }

    @Override
    public BookingDto updateStatus(UpdateBookingStatusDto bookingDto, int ownerId) {
        userStorage.checkUser(ownerId);
        itemStorage.checkItemOwner(ownerId, bookingDto.getItemId());
        bookingStorage.checkBooking(bookingDto.getId());
        log.info("Изменение статуса бронирования");
        Booking updatedBooking = bookingStorage.updateStatus(bookingDto.getId(), bookingDto.getStatus(), ownerId);
        return bookingMapper.mapToBookingDto(updatedBooking);
    }

    @Override
    public BookingDto getBookingById(int bookingId) {
        bookingStorage.checkBooking(bookingId);
        log.info("Запрос бронирования с id = {}", bookingId);
        return bookingMapper.mapToBookingDto(bookingStorage.getBookingById(bookingId));
    }

    @Override
    public List<BookingDto> getOwnersAllBookingsViaStatus(int ownerId, Status status) {
        userStorage.checkUser(ownerId);
        log.info("Запрос бронирований владельца с id = {}", ownerId);
        List<Booking> ownersBookings = bookingStorage.getOwnersAllBookingsViaStatus(ownerId, status);
        return bookingMapper.mapToBookingsDto(ownersBookings);
    }

    @Override
    public List<BookingDto> getBookersAllBooking(int bookerId) {
        userStorage.checkUser(bookerId);
        log.info("Запрос бронирований пользователя с id = {}", bookerId);
        List<Booking> bookersBookings = bookingStorage.getBookersAllBooking(bookerId);
        return bookingMapper.mapToBookingsDto(bookersBookings);
    }

    @Override
    public List<BookingDto> getItemsAllBooking(int itemId) {
        itemStorage.checkItem(itemId);
        log.info("Запрос бронирований предмета с id = {}", itemId);
        List<Booking> itemsBookings = bookingStorage.getItemsAllBooking(itemId);
        return bookingMapper.mapToBookingsDto(itemsBookings);
    }

    @Override
    public void checkTimeForBooking(LocalDateTime start, LocalDateTime end, Integer itemId) {
        itemStorage.checkItem(itemId);

        List<Booking> itemBookings = bookingStorage.getItemsAllBooking(itemId);
        if (itemBookings == null || itemBookings.isEmpty()) {
            return;
        }

        for (Booking booking : itemBookings) {
            if (!end.isBefore(booking.getStart()) || !start.isAfter(booking.getEnd())) {
                log.info("Попытка создать букинг на занятой временной интервал");
                throw new ValidationException("Время букинга данного предмета уже занято");
            }
        }
    }
}
