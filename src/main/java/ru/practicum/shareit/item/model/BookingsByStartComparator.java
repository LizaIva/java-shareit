package ru.practicum.shareit.item.model;

import ru.practicum.shareit.booking.model.Booking;

import java.util.Comparator;

public class BookingsByStartComparator implements Comparator<Booking> {
    @Override
    public int compare(Booking o1, Booking o2) {
        return o1.getStart().compareTo(o2.getEnd());
    }
}
