package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;

@Builder
@Data
public class UpdatedItemDto {

    private String name;
    private String description;
    private Boolean available;
    private ItemRequest request;
}
