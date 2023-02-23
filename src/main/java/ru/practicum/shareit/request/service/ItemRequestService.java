package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto put(ItemRequestDto itemRequestDto);

    ItemRequestDto get(Integer id);

    List<ItemRequestDto> getAll();

    ItemRequestDto delete(Integer id);
}
