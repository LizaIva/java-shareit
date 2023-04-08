package ru.practicum.shareit.gateway.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.gateway.booking.dto.BookingDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Data
public class ItemDto {

    private Integer id;

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    @NotNull
    private Boolean available;

    private Integer requestId;

    private BookingDto lastBooking;

    private BookingDto nextBooking;

    private List<CommentDto> comments;
}