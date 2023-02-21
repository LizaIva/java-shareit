package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

@Data
public class UpdatedItemDto {
    private Integer id;
    private String name;
    private String description;
    private Boolean available;
    private Integer count;

    private ItemRequest request;
}
