package ru.practicum.shareit.server.booking.utils;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.server.booking.dto.BookingDto;
import ru.practicum.shareit.server.booking.dto.CreateBookingRequestDto;
import ru.practicum.shareit.server.booking.model.Booking;
import ru.practicum.shareit.server.item.storage.ItemStorage;
import ru.practicum.shareit.server.item.utils.ItemMapper;
import ru.practicum.shareit.server.user.utils.UserMapper;

import java.util.ArrayList;
import java.util.Comparator;
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

    public static class BookingsByStartComparator implements Comparator<Booking> {
        @Override
        public int compare(Booking o1, Booking o2) {
            return o1.getStart().compareTo(o2.getEnd());
        }
    }
}


