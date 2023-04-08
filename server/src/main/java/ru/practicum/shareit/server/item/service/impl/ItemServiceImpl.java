package ru.practicum.shareit.server.item.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.item.dto.UpdatedItemDto;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.item.service.ItemService;
import ru.practicum.shareit.server.item.storage.ItemStorage;
import ru.practicum.shareit.server.item.utils.ItemMapper;
import ru.practicum.shareit.server.request.model.Response;
import ru.practicum.shareit.server.request.storage.RequestAndResponseStorage;
import ru.practicum.shareit.server.user.storage.UserStorage;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final ItemMapper itemMapper;
    private final UserStorage userStorage;
    private final RequestAndResponseStorage requestAndResponseStorage;

    @Override
    public ItemDto put(Integer ownerId, ItemDto itemDto) {
        userStorage.checkUser(ownerId);
        Item item = itemMapper.mapToItem(ownerId, itemDto);
        log.info("Создание предмета");
        Item savedItem = itemStorage.put(ownerId, item);

        if (itemDto.getRequestId() != null) {
            Response response = Response.builder()
                    .item(savedItem)
                    .owner(userStorage.getUserById(ownerId))
                    .request(requestAndResponseStorage.getRequestById(itemDto.getRequestId()))
                    .build();
            requestAndResponseStorage.put(response);
        }
        return itemMapper.mapToItemDto(savedItem, null);
    }

    @Override
    public ItemDto update(Integer ownerId, Integer itemId, UpdatedItemDto updatedItemDto) {
        userStorage.checkUser(ownerId);
        itemStorage.checkItem(itemId);
        itemStorage.checkItemOwner(ownerId, itemId);
        log.info("Обновление данных предмета с id {}", itemId);

        Item item = itemStorage.getItemById(itemId);

        String name = updatedItemDto.getName();
        if (name != null && !name.isEmpty()) {
            item.setName(name);
        }

        String description = updatedItemDto.getDescription();
        if (description != null && !description.isEmpty()) {
            item.setDescription(description);
        }

        Boolean available = updatedItemDto.getAvailable();
        if (available != null) {
            item.setAvailable(available);
        }

        Item updatedItem = itemStorage.updateItem(ownerId, item);
        return itemMapper.mapToItemDto(updatedItem, null);
    }

    @Override
    public ItemDto getItemById(Integer itemId, Integer userId) {
        userStorage.checkUser(userId);
        itemStorage.checkItem(itemId);

        log.info("Запрос предмета с id = {}", itemId);
        Item item = itemStorage.getItemById(itemId);
        return itemMapper.mapToItemDto(item, userId);
    }

    @Override
    public List<ItemDto> getOwnersItems(Integer ownerId, Integer size, Integer from) {
        log.info("Запрос предметов пользователя с id = {}", ownerId);

        List<Item> items = itemStorage.getAllOwnersItems(ownerId, size, from);
        return itemMapper.mapToItemsDto(items, ownerId);
    }

    @Override
    public List<ItemDto> foundAvailableItemWithNameOrDescription(String description, Integer size, Integer from) {
        if (description == null || description.isEmpty()) {
            return Collections.emptyList();
        }

        log.info("Запрос предметов по ключевому слову {}", description);
        List<Item> items = itemStorage.foundAvailableItemWithNameOrDescription(description, size, from);
        return itemMapper.mapToItemsDto(items, null);
    }
}
