package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.Status;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Data
public class BookingDto {
    private Integer id;

    @PastOrPresent(message = "Время начала бронирования не может быть в будущем")
    @NotNull
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;

    @NotNull
    private Integer itemId;

    private Status status;

    public BookingDto(LocalDateTime start, LocalDateTime end, Integer itemId) {
        this.start = start;
        this.end = end;
        this.itemId = itemId;
    }

    public BookingDto() {
    }
}
