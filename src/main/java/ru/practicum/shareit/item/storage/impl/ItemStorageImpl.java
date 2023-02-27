package ru.practicum.shareit.item.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.CheckOwnerException;
import ru.practicum.shareit.exception.UnknownDataException;
import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class ItemStorageImpl implements ItemStorage {
    private static final String USER_NOT_OWNER_OF_THIS_ITEM_MSG = "item с id = %s не принадлежит пользователю с id = %s";
    private static final String SAVING_EMPTY_ITEM_MSG = "Нельзя сохранить пустой предмет";
    private static final String ITEM_NOT_FOUND_MSG = "item с id = %s не найден.";

    private final UserStorage userStorage;

    private final Map<Integer, Item> items = new HashMap<>();
    private final Map<Integer, Set<Integer>> ownerToItems = new HashMap<>();
    private int counter = 0;

    @Override
    public Item put(Integer ownerId, Item item) {
        if (item == null) {
            throw new UnknownDataException(SAVING_EMPTY_ITEM_MSG);
        }

        if (item.getId() == null) {
            item.setId(++counter);
        }

        items.put(item.getId(), item);
        putItemToOwner(ownerId, item.getId());
        return item;
    }

    @Override
    public Item updateItem(Integer ownerId, Integer itemId, UpdatedItemDto updatedItemDto) {
        checkItem(itemId);
        checkItemOwner(ownerId, itemId);
        Item oldItem = items.get(itemId);
        if (updatedItemDto.getName() != null) {
            oldItem.setName(updatedItemDto.getName());
        }

        if (updatedItemDto.getDescription() != null) {
            oldItem.setDescription(updatedItemDto.getDescription());
        }

        if (updatedItemDto.getAvailable() != null) {
            oldItem.setAvailable(updatedItemDto.getAvailable());
        }

        if (updatedItemDto.getRequest() != null) {
            oldItem.setRequest(updatedItemDto.getRequest());
        }

        return oldItem;
    }

    @Override
    public Item getItemById(Integer id) {
        checkItem(id);
        return items.get(id);
    }

    @Override
    public List<Item> getItemsByIds(List<Integer> ids) {
        List<Item> itemsList = new ArrayList<>();
        for (Integer id : ids) {
            checkItem(id);
            itemsList.add(items.get(id));
        }
        return itemsList;
    }

    @Override
    public List<Item> getAllOwnersItems(Integer ownerId) {
        userStorage.checkUser(ownerId);
        Set<Integer> ownerItems = ownerToItems.get(ownerId);
        if (ownerItems == null || ownerItems.isEmpty()) {
            return null;
        }

        List<Item> usersItems = new ArrayList<>();
        for (Integer id : ownerItems) {
            Item item = items.get(id);
            usersItems.add(item);
        }
        return usersItems;
    }

    @Override
    public List<Item> foundAvailableItemWithNameOrDescription(String description) {
        if (items.isEmpty()) {
            return null;
        }


        List<Item> foundedItems = new ArrayList<>();
        if (description.isEmpty()) {
            return foundedItems;
        }

        String str = description.toLowerCase();
        for (Map.Entry<Integer, Item> entry : items.entrySet()) {
            Item item = entry.getValue();

            if (item.getAvailable()) {
                if (item.getName().toLowerCase().contains(str) ||
                        item.getDescription().toLowerCase().contains(str)) {
                    foundedItems.add(item);
                }
            }
        }
        return foundedItems;
    }

    @Override
    public List<Item> getAll() {
        return new ArrayList<>(items.values());
    }

    @Override
    public Item deleteById(Integer id) {
        checkItem(id);
        return items.remove(id);
    }

    @Override
    public void checkItem(int id) {
        Item item = items.get(id);
        if (item == null) {
            log.info("item с id = {} не найден.", id);
            throw new UnknownDataException(String.format(ITEM_NOT_FOUND_MSG, id));
        }
    }

    @Override
    public void putItemToOwner(int ownerId, int itemId) {
        if (!ownerToItems.containsKey(ownerId)) {
            ownerToItems.put(ownerId, new HashSet<>(Set.of(itemId)));
        } else {
            Set<Integer> items = ownerToItems.get(ownerId);
            items.add(itemId);
        }
    }

    @Override
    public void checkItemOwner(int ownerId, int itemId) {
        Set<Integer> ownerItems = ownerToItems.get(ownerId);
        if (ownerItems == null || ownerItems.isEmpty() || !ownerItems.contains(itemId)) {
            log.info("item с id = {} не принадлежит пользователю с id = {}", itemId, ownerId);
            throw new CheckOwnerException(String.format(USER_NOT_OWNER_OF_THIS_ITEM_MSG, itemId, ownerId));
        }
    }
}
