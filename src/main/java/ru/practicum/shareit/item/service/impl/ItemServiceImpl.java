package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.item.utils.ItemMapper;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final ItemMapper itemMapper;
    private final UserStorage userStorage;

    @Override
    public ItemDto put(Integer ownerId, ItemDto itemDto) {
        userStorage.checkUser(ownerId);
        Item item = itemMapper.mapToItem(ownerId, itemDto);

        log.info("Создание предмета");
        Item savedItem = itemStorage.put(ownerId, item);
        return itemMapper.mapToItemDto(savedItem);
    }

    @Override
    public ItemDto update(Integer ownerId, Integer itemId, UpdatedItemDto updatedItemDto) {
        userStorage.checkUser(ownerId);
        itemStorage.checkItem(itemId);
        itemStorage.checkItemOwner(ownerId, itemId);
        log.info("Обновление данных предмета с id {}", itemId);
        Item updatedItem = itemStorage.updateItem(ownerId, itemId, updatedItemDto);
        return itemMapper.mapToItemDto(updatedItem);
    }

    @Override
    public ItemDto getItemById(Integer id) {
        log.info("Запрос предмета с id = {}", id);
        Item item = itemStorage.getItemById(id);
        return itemMapper.mapToItemDto(item);
    }

    @Override
    public List<ItemDto> getOwnersItems(Integer ownerId) {
        log.info("Запрос предметов пользователя с id = {}", ownerId);
        List<Item> items = itemStorage.getAllOwnersItems(ownerId);
        return itemMapper.mapToItemsDto(items);
    }

    @Override
    public List<ItemDto> foundAvailableItemWithNameOrDescription(String description) {
        log.info("Запрос предметов по ключевому слову {}", description);
        List<Item> items = itemStorage.foundAvailableItemWithNameOrDescription(description);
        return itemMapper.mapToItemsDto(items);
    }
}
