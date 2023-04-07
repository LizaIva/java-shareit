package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.CreateRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;

import java.util.List;

public interface RequestService {
    RequestDto put(CreateRequestDto createRequestDto, Integer requestorId);

    RequestDto get(Integer id, Integer userId);

    List<RequestDto> getAll(Integer userId);

    List<RequestDto> getAllWithOffset(Integer size, Integer from, Integer userId);
}
