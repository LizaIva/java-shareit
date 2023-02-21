package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.request.utils.ItemRequestMapper;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemRequestService {
    private final ItemRequestStorage itemRequestStorage;
    private final ItemRequestMapper itemRequestMapper;

    public ItemRequestDto put(ItemRequestDto itemRequestDto) {
        log.info("Создание запроса");
        ItemRequest itemRequest = itemRequestMapper.mapToItemRequest(itemRequestDto);
        ItemRequest savedItemRequest = itemRequestStorage.put(itemRequest);
        return itemRequestMapper.mapToItemRequestDto(savedItemRequest);
    }

    public ItemRequestDto get(Integer id) {
        log.info("Запрос запроса с id = {}", id);
        ItemRequest itemRequest = itemRequestStorage.getItemRequestById(id);
        return itemRequestMapper.mapToItemRequestDto(itemRequest);
    }

    public List<ItemRequestDto> getAll() {
        log.info("Запрос всех запросов");
        List<ItemRequest> itemRequests = itemRequestStorage.getAll();
        return itemRequestMapper.mapToItemRequestDto(itemRequests);
    }

    public ItemRequestDto delete(Integer id) {
        log.info("Запрос на удаление запроса с id = {}", id);
        ItemRequest itemRequest = itemRequestStorage.delete(id);
        return itemRequestMapper.mapToItemRequestDto(itemRequest);
    }
}
