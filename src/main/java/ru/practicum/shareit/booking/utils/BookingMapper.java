package ru.practicum.shareit.booking.utils;


import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookingMapper {
    public BookingDto mapToBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .itemId(booking.getItemId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .build();
    }

    public Booking mapToBooking(BookingDto bookingDto) {
        return Booking.builder()
                .id(bookingDto.getId())
                .itemId(bookingDto.getItemId())
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
}


