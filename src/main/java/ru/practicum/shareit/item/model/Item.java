package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {
    private Integer id;

    private String name;

    private String description;

    private Boolean available;

    private Integer ownerId;

    private ItemRequest request;
}
