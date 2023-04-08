package ru.practicum.shareit.server.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.server.header.HeaderConst;
import ru.practicum.shareit.server.item.dto.CommentDto;
import ru.practicum.shareit.server.item.dto.CreateCommentDto;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.item.dto.UpdatedItemDto;
import ru.practicum.shareit.server.item.service.CommentService;
import ru.practicum.shareit.server.item.service.ItemService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;
    private final CommentService commentService;

    @PostMapping
    public ItemDto create(
            @RequestHeader(HeaderConst.USER_ID_HEADER) Integer ownerId,
            @RequestBody ItemDto itemDto) {
        log.info("Получен запрос на создание предмета");
        return itemService.put(ownerId, itemDto);
    }

    @PostMapping("{itemId}/comment")
    public CommentDto createComment(
            @PathVariable Integer itemId,
            @RequestHeader(HeaderConst.USER_ID_HEADER) Integer userId,
            @RequestBody CreateCommentDto commentDto) {
        log.info("Получен запрос на создание отзыва к предмету с id = {}", itemId);
        return commentService.put(itemId, userId, commentDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(
            @RequestHeader(HeaderConst.USER_ID_HEADER) Integer ownerId,
            @PathVariable Integer itemId,
            @RequestBody UpdatedItemDto updatedItemDto) {
        log.info("Получен запрос на обновление предмета");
        return itemService.update(ownerId, itemId, updatedItemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(
            @RequestHeader(HeaderConst.USER_ID_HEADER) Integer userId,
            @PathVariable Integer itemId
    ) {
        log.info("Получен запрос предмета с id = {}", itemId);
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getOwnersItems(@RequestHeader(HeaderConst.USER_ID_HEADER) Integer ownerId,
                                        @RequestParam(name = "from", required = false) Integer from,
                                        @RequestParam(name = "size", required = false) Integer size) {
        log.info("Получен запрос все предметов пользователя с id = {}", ownerId);
        return itemService.getOwnersItems(ownerId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> foundItemWithText(@RequestParam String text,
                                           @RequestParam(name = "from", required = false) Integer from,
                                           @RequestParam(name = "size", required = false) Integer size
    ) {
        log.info("Запрос предметов по ключевому слову {}", text);
        return itemService.foundAvailableItemWithNameOrDescription(text, from, size);
    }
}



