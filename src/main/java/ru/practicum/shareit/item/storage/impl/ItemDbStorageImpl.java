package ru.practicum.shareit.item.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

    private static final Sort PAGIN_SORT = Sort.by(List.of(Sort.Order.asc("item_id")));

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
    public List<Item> getAllOwnersItems(Integer ownerId, Integer size, Integer from) {
        userStorage.checkUser(ownerId);

        if (size == null || from == null) {
            return itemRepository.findByOwnerIdOrderById(ownerId);
        }

        return itemRepository.findByOwnerIdOrderById(ownerId, PageRequest.of(from, size, PAGIN_SORT))
                .getContent();
    }

    @Override
    public List<Item> foundAvailableItemWithNameOrDescription(String description, Integer size, Integer from) {
        if (size == null || from == null) {
            return itemRepository.foundAvailableItemWithNameOrDescription(description.toLowerCase());
        }

        return itemRepository.foundAvailableItemWithNameOrDescription(description.toLowerCase(),
                        PageRequest.of(from, size, PAGIN_SORT))
                .getContent();
    }

    @Override
    public Item updateItem(Integer ownerId, Item item) {
        checkItemOwner(ownerId, item.getId());
        return itemRepository.saveAndFlush(item);
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
