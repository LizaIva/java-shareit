package ru.practicum.shareit.gateway.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.gateway.request.dto.RequestDto;

@Builder
@Data
public class UpdatedItemDto {

    private String name;
    private String description;
    private Boolean available;
    private RequestDto request;
}