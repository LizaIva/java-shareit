package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.Status;

@Builder
@Data
public class UpdateBookingStatusDto {

    private Integer id;

    private Status status;

    private Integer itemId;
}
