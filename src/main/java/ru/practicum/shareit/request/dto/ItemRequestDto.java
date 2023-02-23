package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Builder
@Data
public class ItemRequestDto {

    private Integer id;

    @NotBlank(message = "Описание не может быть пустым")
    private String description;

    private Integer requestorId;

    @PastOrPresent(message = "Время создания запроса не может быть в будущем")
    private LocalDateTime created;
}
