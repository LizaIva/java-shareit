package ru.practicum.shareit.request.utils;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.ArrayList;
import java.util.List;

@Component
public class ItemRequestMapper {
    public ItemRequestDto mapToItemRequestDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestorId(itemRequest.getRequestorId())
                .created(itemRequest.getCreated())
                .build();
    }

    public ItemRequest mapToItemRequest(ItemRequestDto itemRequestDto) {
        return ItemRequest.builder()
                .id(itemRequestDto.getId())
                .description(itemRequestDto.getDescription())
                .requestorId(itemRequestDto.getRequestorId())
                .created(itemRequestDto.getCreated())
                .build();
    }

    public List<ItemRequestDto> mapToItemRequestDto(List<ItemRequest> itemRequests) {
        List<ItemRequestDto> itemRequestsDto = new ArrayList<>();
        for (ItemRequest itemRequest : itemRequests) {
            itemRequestsDto.add(mapToItemRequestDto(itemRequest));
        }
        return itemRequestsDto;
    }
}
