package ru.practicum.shareit.gateway.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.gateway.header.HeaderConst;
import ru.practicum.shareit.gateway.item.dto.CreateCommentDto;
import ru.practicum.shareit.gateway.item.dto.ItemDto;
import ru.practicum.shareit.gateway.item.dto.UpdatedItemDto;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestHeader(HeaderConst.USER_ID_HEADER) Integer ownerId,
            @RequestBody @Valid ItemDto itemDto) {
        log.info("Получен запрос на создание предмета");
        return itemClient.createItem(ownerId, itemDto);
    }

    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> createComment(
            @PathVariable Integer itemId,
            @RequestHeader(HeaderConst.USER_ID_HEADER) Integer userId,
            @RequestBody @Valid CreateCommentDto commentDto) {
        log.info("Получен запрос на создание отзыва к предмету с id = {}", itemId);
        return itemClient.createComment(itemId, userId, commentDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(
            @RequestHeader(HeaderConst.USER_ID_HEADER) Integer ownerId,
            @PathVariable Integer itemId,
            @RequestBody @Valid UpdatedItemDto updatedItemDto) {
        log.info("Получен запрос на обновление предмета");
        return itemClient.update(ownerId, itemId, updatedItemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(
            @RequestHeader(HeaderConst.USER_ID_HEADER) Integer userId,
            @PathVariable Integer itemId
    ) {
        log.info("Получен запрос предмета с id = {}", itemId);
        return itemClient.getItemById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getOwnersItems(@RequestHeader(HeaderConst.USER_ID_HEADER) Integer ownerId,
                                                 @RequestParam(name = "from", required = false) Integer from,
                                                 @RequestParam(name = "size", required = false) Integer size) {
        log.info("Получен запрос все предметов пользователя с id = {}", ownerId);
        return itemClient.getOwnersItems(ownerId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> foundItemWithText(@RequestParam String text,
                                                    @RequestParam(name = "from", required = false) Integer from,
                                                    @RequestParam(name = "size", required = false) Integer size
    ) {
        log.info("Запрос предметов по ключевому слову {}", text);
        return itemClient.foundItemWithText(text, from, size);
    }
}



