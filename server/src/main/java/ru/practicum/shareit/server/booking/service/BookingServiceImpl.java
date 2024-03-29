package ru.practicum.shareit.server.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.server.booking.dto.BookingDto;
import ru.practicum.shareit.server.booking.dto.CreateBookingRequestDto;
import ru.practicum.shareit.server.booking.dto.State;
import ru.practicum.shareit.server.booking.model.Booking;
import ru.practicum.shareit.server.booking.model.Status;
import ru.practicum.shareit.server.booking.storage.BookingStorage;
import ru.practicum.shareit.server.booking.utils.BookingMapper;
import ru.practicum.shareit.server.exception.UnknownDataException;
import ru.practicum.shareit.server.exception.ValidationException;
import ru.practicum.shareit.server.item.storage.ItemStorage;
import ru.practicum.shareit.server.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final BookingStorage bookingStorage;
    private final BookingMapper bookingMapper;

    @Override
    public BookingDto put(CreateBookingRequestDto bookingDto, Integer bookerId) {
        userStorage.checkUser(bookerId);
        if (Objects.equals(itemStorage.getItemById(bookingDto.getItemId()).getOwner().getId(), bookerId)) {
            throw new UnknownDataException("Владелец вещи не может арендовать свою вещь");
        }

        log.info("Создание бронирования");
        checkTimeForBooking(bookingDto.getStart(), bookingDto.getEnd(), bookingDto.getItemId());
        Booking booking = bookingMapper.mapToBooking(bookingDto);

        booking.setStatus(Status.WAITING);
        Booking savedBooking = bookingStorage.put(booking, bookerId);
        return bookingMapper.mapToBookingDto(savedBooking);
    }

    @Override
    public BookingDto updateStatus(int bookingId, boolean approved, int ownerId) {
        bookingStorage.checkBooking(bookingId);

        bookingStorage.checkUserItemOwnerByBookingId(bookingId, ownerId);

        Booking bookingById = bookingStorage.getBookingById(bookingId, ownerId);
        if (Status.APPROVED.equals(bookingById.getStatus()) || Status.REJECTED.equals(bookingById.getStatus())) {
            throw new ValidationException(String.format("Booking with id %s has status APPROVED", bookingId));
        }

        log.info("Изменение статуса бронирования");
        Booking updatedBooking = bookingStorage.updateStatus(
                bookingId,
                approved ? Status.APPROVED : Status.REJECTED,
                ownerId
        );

        return bookingMapper.mapToBookingDto(updatedBooking);
    }

    @Override
    public BookingDto getBookingById(int bookingId, int userId) {
        bookingStorage.checkBooking(bookingId);
        log.info("Запрос бронирования с id = {}", bookingId);
        return bookingMapper.mapToBookingDto(bookingStorage.getBookingById(bookingId, userId));
    }

    @Override
    public List<BookingDto> getOwnersAllBookingsViaStatus(int ownerId, State state, Integer size, Integer from) {
        userStorage.checkUser(ownerId);


        log.info("Запрос бронирований владельца с id = {}", ownerId);


        List<Booking> ownersBookings = bookingStorage.getOwnersAllBookings(ownerId, size, from);
        return bookingMapper.mapToBookingsDto(filterBookingsByState(state, ownersBookings));
    }

    private List<Booking> filterBookingsByState(State state, List<Booking> bookings) {

        List<Booking> filteredBookings = new ArrayList<>();
        LocalDateTime timeNow = LocalDateTime.now();

        switch (state) {
            case ALL:
                return bookings;
            case CURRENT:
                for (Booking booking : bookings) {
                    if (!booking.getStart().isAfter(timeNow) && !booking.getEnd().isBefore(timeNow)) {
                        filteredBookings.add(booking);
                    }
                }

                filteredBookings.sort(Comparator.comparing(Booking::getId));
                break;
            case PAST:
                for (Booking booking : bookings) {
                    if (booking.getEnd().isBefore(timeNow)) {
                        filteredBookings.add(booking);
                    }
                }

                filteredBookings.sort(Comparator.comparing(Booking::getStart).reversed());
                break;
            case FUTURE:
                for (Booking booking : bookings) {
                    if (!Status.REJECTED.equals(booking.getStatus()) && booking.getStart().isAfter(timeNow)) {
                        filteredBookings.add(booking);
                    }
                }

                filteredBookings.sort(Comparator.comparing(Booking::getStart).reversed());
                break;
            case WAITING:
                for (Booking booking : bookings) {
                    if (Status.WAITING.equals(booking.getStatus())) {
                        filteredBookings.add(booking);
                    }
                }

                filteredBookings.sort(Comparator.comparing(Booking::getStart));
                break;
            case REJECTED:
                for (Booking booking : bookings) {
                    if (Status.REJECTED.equals(booking.getStatus())) {
                        filteredBookings.add(booking);
                    }
                }

                filteredBookings.sort(Comparator.comparing(Booking::getStart));
                break;
            default:
                throw new UnknownDataException(String.format("Передан неизвестный стэйт %s", state));
        }

        return filteredBookings;
    }

    @Override
    public List<BookingDto> getBookersAllBooking(int bookerId, State state, Integer size, Integer from) {
        userStorage.checkUser(bookerId);

        log.info("Запрос бронирований пользователя с id = {}", bookerId);
        List<Booking> bookersBookings = bookingStorage.getBookersAllBooking(bookerId, size, from);

        return bookingMapper.mapToBookingsDto(filterBookingsByState(state, bookersBookings));
    }

    @Override
    public void checkTimeForBooking(LocalDateTime start, LocalDateTime end, Integer itemId) {
        itemStorage.checkItem(itemId);

        if (end.isBefore(start)) {
            log.info("Попытка создать букинг с неверным временем старта и окончания");
            throw new ValidationException("Время окончания букинга раньше старта букинга");
        }

        if (start.equals(end)) {
            log.info("Попытка создать букинг с одинаковым временем старта и окончания");
            throw new ValidationException("Время старта и окончания букинга не может быть одинаковым");
        }
    }
}
