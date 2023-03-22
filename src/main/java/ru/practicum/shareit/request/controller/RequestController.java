package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.CreateRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.service.RequestService;

import javax.validation.Valid;
import java.util.List;

import static ru.practicum.shareit.header.HeaderConst.USER_ID_HEADER;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class RequestController {

    private final RequestService requestService;

    @PostMapping
    public RequestDto create(@RequestHeader(USER_ID_HEADER) Integer requestorId,
                             @RequestBody @Valid CreateRequestDto createRequestDto) {
        log.info("Получен запрос на создание запроса");
        return requestService.put(createRequestDto, requestorId);
    }

    @GetMapping("/{requestId}")
    public RequestDto getRequestById(@RequestHeader(USER_ID_HEADER) Integer userId,
                                     @PathVariable Integer requestId) {
        log.info("Получен запрос на вывод запроса с id = {}", requestId);
        return requestService.get(requestId, userId);
    }

    @GetMapping
    public List<RequestDto> getAll(@RequestHeader(USER_ID_HEADER) Integer userId) {
        log.info("Получен запрос на вывод всех запросов");
        return requestService.getAll(userId);
    }

    @GetMapping("/all")
    public List<RequestDto> getAllWithOffset(@RequestHeader(USER_ID_HEADER) Integer userId,
                                             @RequestParam(name = "from", required = false) Integer from,
                                             @RequestParam(name = "size", required = false) Integer size) {
        log.info("Получен запрос на вывод всех запросов");
        return requestService.getAllWithOffset(size, from, userId);
    }
}
