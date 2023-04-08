package ru.practicum.shareit.server.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.server.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {

    private Integer id;

    private String description;

    private LocalDateTime created;

    private List<ResponseDto> responses;

    private List<ItemDto> items;
}
