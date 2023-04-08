package ru.practicum.shareit.server.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.server.request.model.Request;

@Builder
@Data
public class UpdatedItemDto {

    private String name;
    private String description;
    private Boolean available;
    private Request request;
}
