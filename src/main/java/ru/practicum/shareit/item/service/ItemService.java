package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdatedItemDto;

import java.util.List;

public interface ItemService {
    ItemDto put(Integer ownerId, ItemDto itemDto);

    ItemDto update(Integer ownerId, Integer itemId, UpdatedItemDto updatedItemDto);

    ItemDto getItemById(Integer itemId, Integer userId);

    List<ItemDto> getOwnersItems(Integer ownerId, Integer size, Integer from);

    List<ItemDto> foundAvailableItemWithNameOrDescription(String description, Integer size, Integer from);
}
