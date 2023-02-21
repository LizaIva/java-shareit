package ru.practicum.shareit.request.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.UnknownDataException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class ItemRequestStorageImpl implements ItemRequestStorage {
    private final Map<Integer, ItemRequest> itemRequests = new HashMap<>();
    private int counter = 0;

    @Override
    public ItemRequest put(ItemRequest itemRequest) {
        if (itemRequest == null) {
            throw new UnknownDataException("Нельзя сохранить запрос без данных");
        }

        if (itemRequest.getId() == null) {
            itemRequest.setId(++counter);
        }

        if (itemRequest.getCreated() == null) {
            itemRequest.setCreated(LocalDateTime.now());
        }

        itemRequests.put(itemRequest.getId(), itemRequest);
        return itemRequest;

    }

    @Override
    public ItemRequest getItemRequestById(Integer id) {
        checkItemRequest(id);
        return itemRequests.get(id);
    }

    @Override
    public List<ItemRequest> getAll() {
        return new ArrayList<>(itemRequests.values());
    }

    @Override
    public ItemRequest delete(Integer id) {
        checkItemRequest(id);
        return itemRequests.remove(id);

    }

    @Override
    public void checkItemRequest(int id) {
        ItemRequest itemRequest = itemRequests.get(id);
        if (itemRequest == null) {
            log.info("ItemRequest с id = {} не найден.", id);
            throw new UnknownDataException("ItemRequest с id = " + id + " не найден.");
        }
    }
}
