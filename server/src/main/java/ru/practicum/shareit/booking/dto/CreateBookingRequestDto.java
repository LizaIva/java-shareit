package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;

@Builder
@Data
public class CreateBookingRequestDto {

    private Integer id;


    private LocalDateTime start;

    private LocalDateTime end;

    private Integer itemId;

    private Status status;
}
