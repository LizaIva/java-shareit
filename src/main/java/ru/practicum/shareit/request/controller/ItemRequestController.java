package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto create(@RequestBody @Valid ItemRequestDto itemRequestDto) {
        log.info("Получен запрос на создание запроса");
        return itemRequestService.put(itemRequestDto);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getUserById(@PathVariable Integer requestId) {
        log.info("Получен запрос на вывод запроса с id = {}", requestId);
        return itemRequestService.get(requestId);
    }

    @GetMapping
    public List<ItemRequestDto> getAll() {
        log.info("Получен запрос на вывод всех запросов");
        return itemRequestService.getAll();
    }

    @DeleteMapping("/{id}")
    public ItemRequestDto deleteById(@PathVariable int id) {
        log.info("Получен запрос на удаление запроса с id = {}", id);
        return itemRequestService.delete(id);
    }
}
