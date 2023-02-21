package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;

@Data
public class UpdatedItemDto {
    private String name;
    private String description;
    private Boolean available;
    private ItemRequest request;

    public UpdatedItemDto(String name, String description, Boolean available) {
        this.name = name;
        this.description = description;
        this.available = available;
    }

    public UpdatedItemDto() {
    }
}
