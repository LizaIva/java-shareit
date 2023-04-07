package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.CreateRequestDto;

import javax.validation.Valid;

import static ru.practicum.shareit.header.HeaderConst.USER_ID_HEADER;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {

    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(USER_ID_HEADER) Integer requestorId,
                                         @RequestBody @Valid CreateRequestDto createRequestDto) {
        log.info("Получен запрос на создание запроса");
        return requestClient.createRequest(requestorId, createRequestDto);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader(USER_ID_HEADER) Integer userId,
                                                 @PathVariable Integer requestId) {
        log.info("Получен запрос на вывод запроса с id = {}", requestId);
        return requestClient.getRequestById(userId, requestId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@RequestHeader(USER_ID_HEADER) Integer userId) {
        log.info("Получен запрос на вывод всех запросов");
        return requestClient.getAll(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllWithOffset(@RequestHeader(USER_ID_HEADER) Integer userId,
                                                   @RequestParam(name = "from", required = false) Integer from,
                                                   @RequestParam(name = "size", required = false) Integer size) {
        log.info("Получен запрос на вывод всех запросов");
        return requestClient.getAllWithOffset(userId, from, size);
    }
}
