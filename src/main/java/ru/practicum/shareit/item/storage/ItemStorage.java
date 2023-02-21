package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    Item put(Integer ownerId, Item item);

    Item getItemById(Integer id);

    List<Item> getItemsByIds(List<Integer> ids);

    List<Item> getAllOwnersItems(Integer ownerId);

    List<Item> foundAvailableItemWithNameOrDescription(String description);

    List<Item> getAll();

    Item updateItem(Integer ownerId, Integer itemId, UpdatedItemDto updatedItemDto);

    Item deleteById(Integer id);

    void checkItem(int id);

    void putItemToOwner(int ownerId, int itemId);

    void checkItemOwner(int ownerId, int itemId);
}
