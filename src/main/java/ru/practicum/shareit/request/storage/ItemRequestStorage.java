package ru.practicum.shareit.request.storage;

import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestStorage {
    ItemRequest put(ItemRequest itemRequest);

    ItemRequest getItemRequestById(Integer id);

    List<ItemRequest> getAll();

    ItemRequest delete(Integer id);

    void checkItemRequest(int id);
}
