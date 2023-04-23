package ru.practicum.shareit.gateway.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.gateway.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {

    private Integer id;

    @NotBlank(message = "Описание не может быть пустым")
    private String description;


    @PastOrPresent(message = "Время создания запроса не может быть в будущем")
    private LocalDateTime created;

    private List<ResponseDto> responses;

    private List<ItemDto> items;
}