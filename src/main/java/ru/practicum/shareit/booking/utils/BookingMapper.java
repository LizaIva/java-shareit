package ru.practicum.shareit.booking.utils;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.CreateBookingRequestDto;
import ru.practicum.shareit.booking.dto.State;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.exception.UnknownDataException;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.item.utils.ItemMapper;
import ru.practicum.shareit.user.utils.UserMapper;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingMapper {

    private final ItemStorage itemStorage;
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;

    public BookingDto mapToBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .booker(userMapper.mapToUserDto(booking.getBooker()))
                .item(itemMapper.mapToItemDto(booking.getBookedItem(), null))
                .bookerId(booking.getBooker().getId())
                .itemId(booking.getBookedItem().getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .build();
    }

    public Booking mapToBooking(CreateBookingRequestDto bookingDto) {
        return Booking.builder()
                .id(bookingDto.getId())
                .bookedItem(itemStorage.getItemById(bookingDto.getItemId()))
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .status(bookingDto.getStatus())
                .build();
    }

    public List<BookingDto> mapToBookingsDto(List<Booking> bookings) {
        if (bookings == null) {
            return null;
        }
        List<BookingDto> bookingsDto = new ArrayList<>();
        for (Booking booking : bookings) {
            bookingsDto.add(mapToBookingDto(booking));
        }
        return bookingsDto;
    }

    public int mapStatus(Status status) {
        switch (status) {
            case WAITING:
                return 0;
            case APPROVED:
                return 1;
            case REJECTED:
                return 2;
            case CANCELED:
                return 3;
            default:
                throw new UnknownDataException(String.format("Передан неизвестный статус %s", status));
        }
    }

    public List<Integer> mapStateToStatus(State state) {
        switch (state) {
            case ALL:
                return List.of(0, 1, 2, 3);
            case CURRENT:
                return List.of(1);
            case PAST:
                return List.of(2, 3);
            case FUTURE:
                return List.of(0, 1);
            case WAITING:
                return List.of(0);
            case REJECTED:
                return List.of(2);
            default:
                throw new UnknownDataException(String.format("Unknown state: %s", state));
        }
    }
}


