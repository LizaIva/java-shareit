package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.model.Request;

@Builder
@Data
public class UpdatedItemDto {

    private String name;
    private String description;
    private Boolean available;
    private Request request;
}
