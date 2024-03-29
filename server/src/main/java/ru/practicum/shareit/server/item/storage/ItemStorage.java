package ru.practicum.shareit.server.item.storage;

import ru.practicum.shareit.server.item.model.Item;

import java.util.List;

public interface ItemStorage {
    String ITEM_NOT_FOUND = "item с id = %s не найден.";
    String USER_NOT_OWNER_OF_THIS_ITEM_MSG = "item с id = %s не принадлежит пользователю с id = %s";

    Item put(Integer ownerId, Item item);

    Item getItemById(Integer id);

    List<Item> getAllOwnersItems(Integer ownerId, Integer size, Integer from);

    List<Item> foundAvailableItemWithNameOrDescription(String description, Integer size, Integer from);

    Item updateItem(Integer ownerId, Item item);

    void checkItem(int id);

    void checkItemOwner(int ownerId, int itemId);
}
