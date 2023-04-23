package ru.practicum.shareit.gateway.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.gateway.item.dto.ItemDto;
import ru.practicum.shareit.gateway.user.dto.UserDto;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@Data
public class BookingDto {

    private Integer id;

    @NotNull
    @FutureOrPresent
    private LocalDateTime start;

    @NotNull
    @FutureOrPresent
    private LocalDateTime end;

    private ItemDto item;

    private UserDto booker;

    private int itemId;

    private int bookerId;

    private Status status;
}