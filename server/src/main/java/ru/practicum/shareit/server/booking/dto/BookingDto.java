package ru.practicum.shareit.server.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.server.booking.model.Status;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.user.dto.UserDto;

import java.time.LocalDateTime;

@Builder
@Data
public class BookingDto {

    private Integer id;

    private LocalDateTime start;

    private LocalDateTime end;

    private ItemDto item;

    private UserDto booker;

    private int itemId;

    private int bookerId;

    private Status status;
}
