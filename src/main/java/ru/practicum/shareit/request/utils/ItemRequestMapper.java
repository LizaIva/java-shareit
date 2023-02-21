package ru.practicum.shareit.request.utils;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemRequestMapper {
    public ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getDescription());
        itemRequestDto.setRequestorId(itemRequest.getRequestorId());
        itemRequestDto.setCreated(itemRequest.getCreated());
        return itemRequestDto;
    }

    public ItemRequest mapToItemRequest(ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(itemRequestDto.getId());
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setRequestorId(itemRequestDto.getRequestorId());
        itemRequest.setCreated(itemRequestDto.getCreated());
        return itemRequest;
    }

    public List<ItemRequestDto> mapToItemRequestDto(List<ItemRequest> itemRequests) {
        List<ItemRequestDto> itemRequestsDto = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            itemRequestsDto.add(mapToItemRequestDto(itemRequest));
        }
        return itemRequestsDto;
    }
}
