package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto create(
            @RequestHeader("X-Sharer-User-Id") Integer ownerId,
            @RequestBody @Valid ItemDto itemDto) {
        log.info("Получен запрос на создание предмета");
        return itemService.put(ownerId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(
            @RequestHeader("X-Sharer-User-Id") Integer ownerId,
            @PathVariable Integer itemId,
            @RequestBody @Valid UpdatedItemDto updatedItemDto) {
        log.info("Получен запрос на обновление предмета");
        return itemService.update(ownerId, itemId, updatedItemDto);
    }


    @GetMapping("/{itemId}")
    public ItemDto getItemById(
            @RequestHeader("X-Sharer-User-Id") Integer ownerId,
            @PathVariable Integer itemId
    ) {
        log.info("Получен запрос предмета с id = {}", itemId);
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<ItemDto> getOwnersItems(@RequestHeader("X-Sharer-User-Id") Integer ownerId) {
        log.info("Получен запрос все предметов пользователя с id = {}", ownerId);
        return itemService.getOwnersItems(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> foundItemWithText(@RequestParam String text) {
        log.info("Запрос предметов по ключевому слову {}", text);
        return itemService.foundAvailableItemWithNameOrDescription(text);
    }

}


// На случай, если нужной вещи на сервисе нет, у пользователей должна быть возможность оставлять запросы.
// По запросу можно будет добавлять новые вещи для шеринга.



