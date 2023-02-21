package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.Status;

@Data
public class UpdateBookingStatusDto {

    private Integer id;

    private Status status;

    private Integer itemId;

    public UpdateBookingStatusDto(Integer id, Status status, Integer itemId) {
        this.id = id;
        this.status = status;
        this.itemId = itemId;
    }
}
