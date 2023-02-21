package ru.practicum.shareit.item.utils;


import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemMapper {

    public ItemDto mapToItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setCount(itemDto.getCount());
        itemDto.setRequest(item.getRequest());
        return itemDto;
    }

    public Item mapToItem(Integer ownerId, ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setOwnerId(ownerId);
        item.setAvailable(itemDto.getAvailable());

        item.setRequest(itemDto.getRequest());
        return item;
    }


    public List<ItemDto> mapToItemsDto(List<Item> items) {
        List<ItemDto> itemsDto = new ArrayList<>();
        for (Item item : items) {
            itemsDto.add(mapToItemDto(item));
        }
        return itemsDto;
    }
}
