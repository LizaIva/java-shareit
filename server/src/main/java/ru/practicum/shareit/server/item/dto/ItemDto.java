package ru.practicum.shareit.server.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.server.booking.dto.BookingDto;

import java.util.List;

@Builder
@Data
public class ItemDto {

    private Integer id;

    private String name;

    private String description;

    private Boolean available;

    private Integer requestId;

    private BookingDto lastBooking;

    private BookingDto nextBooking;

    private List<CommentDto> comments;
}
