package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.UpdateBookingStatusDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.booking.utils.BookingMapper;
import ru.practicum.shareit.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingStorage bookingStorage;
    private final BookingMapper bookingMapper;

    public BookingDto put(BookingDto bookingDto, Integer bookerId) {
        log.info("Создание бронирования");
        checkTimeForBooking(bookingDto.getStart(), bookingDto.getEnd(), bookingDto.getItemId());
        Booking booking = bookingMapper.mapToBooking(bookingDto);
        Booking savedBooking = bookingStorage.put(booking, bookerId);
        return bookingMapper.mapToBookingDto(savedBooking);
    }

    public BookingDto updateStatus(UpdateBookingStatusDto bookingDto, int ownerId) {
        log.info("Изменение статуса бронирования");
        Booking updatedBooking = bookingStorage.updateStatus(bookingDto.getId(), bookingDto.getStatus(), ownerId);
        return bookingMapper.mapToBookingDto(updatedBooking);
    }

    public BookingDto getBookingById(int bookingId) {
        log.info("Запрос бронирования с id = {}", bookingId);
        return bookingMapper.mapToBookingDto(bookingStorage.getBookingById(bookingId));
    }

    public List<BookingDto> getOwnersAllBookingsViaStatus(int ownerId, Status status) {
        log.info("Запрос бронирований владельца с id = {}", ownerId);
        List<Booking> ownersBookings = bookingStorage.getOwnersAllBookingsViaStatus(ownerId, status);
        return bookingMapper.mapToBookingsDto(ownersBookings);
    }

    public List<BookingDto> getBookersAllBooking(int bookerId) {
        log.info("Запрос бронирований пользователя с id = {}", bookerId);
        List<Booking> bookersBookings = bookingStorage.getBookersAllBooking(bookerId);
        return bookingMapper.mapToBookingsDto(bookersBookings);
    }

    public List<BookingDto> getItemsAllBooking(int itemId) {
        log.info("Запрос бронирований предмета с id = {}", itemId);
        List<Booking> itemsBookings = bookingStorage.getBookersAllBooking(itemId);
        return bookingMapper.mapToBookingsDto(itemsBookings);
    }


    public void checkTimeForBooking(LocalDateTime start, LocalDateTime end, Integer itemId) {
        // может упорядочить по времени старта?
        List<Booking> itemBookings = bookingStorage.getItemsAllBooking(itemId);
        if (itemBookings.isEmpty()) {
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
