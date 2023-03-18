package ru.practicum.shareit.item.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.CheckOwnerException;
import ru.practicum.shareit.exception.UnknownDataException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;


@RequiredArgsConstructor
@Component("itemDbStorageImpl")
public class ItemDbStorageImpl implements ItemStorage {

    private final ItemRepository itemRepository;
    private final UserStorage userStorage;

    @Override
    public Item put(Integer ownerId, Item item) {
        return itemRepository.saveAndFlush(item);
    }

    @Override
    public Item getItemById(Integer id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new UnknownDataException(String.format(ITEM_NOT_FOUND, id)));
    }

    @Override
    public List<Item> getAllOwnersItems(Integer ownerId) {
        userStorage.checkUser(ownerId);
        return itemRepository.findByOwnerIdOrderById(ownerId);
    }

    @Override
    public List<Item> foundAvailableItemWithNameOrDescription(String description) {
        return itemRepository.foundAvailableItemWithNameOrDescription(description.toLowerCase());
    }

    @Override
    public List<Item> getAll() {
        return itemRepository.findAll();
    }

    @Override
    public Item updateItem(Integer ownerId, Item item) {
        checkItemOwner(ownerId, item.getId());
        return itemRepository.saveAndFlush(item);
    }


    @Override
    public Item deleteById(Integer id) {
        Item deletedItem = getItemById(id);
        itemRepository.deleteById(id);
        return deletedItem;
    }

    @Override
    public void checkItem(int id) {
        if (!itemRepository.existsById(id)) {
            throw new UnknownDataException(String.format(ITEM_NOT_FOUND, id));
        }
    }

    @Override
    public void checkItemOwner(int ownerId, int itemId) {
        if (getItemById(itemId).getOwner().getId() != ownerId) {
            throw new CheckOwnerException(String.format(USER_NOT_OWNER_OF_THIS_ITEM_MSG, itemId, ownerId));
        }
    }
}
