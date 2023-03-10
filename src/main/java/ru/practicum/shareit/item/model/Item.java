package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;

@Builder
@Data
public class Item {

    private Integer id;

    private String name;

    private String description;

    private Boolean available;

    private Integer ownerId;

    private ItemRequest request;
}
