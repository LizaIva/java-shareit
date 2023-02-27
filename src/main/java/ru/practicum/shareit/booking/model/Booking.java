package ru.practicum.shareit.booking.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class Booking {

    private Integer id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Integer itemId;

    private Integer bookerId;

    private Status status;
}
