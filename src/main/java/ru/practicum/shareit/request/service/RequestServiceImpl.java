package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.request.dto.CreateRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.storage.RequestAndResponseStorage;
import ru.practicum.shareit.request.utils.RequestMapper;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestAndResponseStorage requestAndResponseStorage;
    private final RequestMapper requestMapper;
    private final UserStorage userStorage;

    @Override
    public RequestDto put(CreateRequestDto createRequestDto, Integer requestorId) {
        userStorage.checkUser(requestorId);
        log.info("Создание запроса");
        Request request = requestMapper.mapToRequest(requestorId, createRequestDto);
        Request savedRequest = requestAndResponseStorage.put(request);
        return requestMapper.mapToRequestsDto(savedRequest, requestorId);
    }

    @Override
    public RequestDto get(Integer id, Integer userId) {
        if (userId != null) {
            userStorage.checkUser(userId);
        }

        log.info("Запрос запроса с id = {}", id);
        Request request = requestAndResponseStorage.getRequestById(id);
        return requestMapper.mapToRequestsDto(request, request.getRequestId());
    }

    @Override
    public List<RequestDto> getAll(Integer userId) {
        log.info("Запрос всех запросов");

        userStorage.checkUser(userId);

        List<Request> requests = requestAndResponseStorage.getAll();
        return requestMapper.mapToRequestsDto(requests, userId);
    }

    @Override
    public List<RequestDto> getAllWithOffset(Integer size, Integer from, Integer userId) {
        if (size == null || from == null) {
            return getAll(userId);
        }

        if (size <= 0 || from < 0) {
            throw new ValidationException("Нельзя передавать отрицательные значения");
        }

        List<Request> pagedRequests;
        if (userId == null) {
            pagedRequests = requestAndResponseStorage.getAllWithOffset(size, from);
        } else {
            pagedRequests = requestAndResponseStorage.getAllByUserIdWithOffset(size, from, userId);
        }

        return requestMapper.mapToRequestsDto(pagedRequests, userId);
    }
}
