package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdatedItemDto;

import java.util.List;

public interface ItemService {
    ItemDto put(Integer ownerId, ItemDto itemDto);

    ItemDto update(Integer ownerId, Integer itemId, UpdatedItemDto updatedItemDto);

    ItemDto getItemById(Integer id);

    List<ItemDto> getOwnersItems(Integer ownerId);

    List<ItemDto> foundAvailableItemWithNameOrDescription(String description);
}
