package ru.practicum.shareit.request.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.utils.ItemMapper;
import ru.practicum.shareit.request.dto.CreateRequestDto;
import ru.practicum.shareit.request.dto.RequestDto;
import ru.practicum.shareit.request.model.Request;
import ru.practicum.shareit.request.model.Response;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RequestMapper {

    private final UserStorage userStorage;
    private final ResponseMapper responseMapper;
    private final ItemMapper itemMapper;

    public RequestDto mapToRequestsDto(Request request, Integer userId) {
        return RequestDto.builder()
                .id(request.getRequestId())
                .description(request.getDescription())
                .created(request.getCreated())
                .responses(responseMapper.mapToResponsesDto(request.getResponses()))
                .items(mapItemsFromResponse(request.getResponses(), userId))
                .build();
    }

    private List<ItemDto> mapItemsFromResponse(List<Response> responses, Integer userId) {
        List<ItemDto> itemDtos = new ArrayList<>();

        if (responses == null || responses.isEmpty()) {
            return itemDtos;
        }

        for (Response response : responses) {
            itemDtos.add(itemMapper.mapToItemDto(response.getItem(), userId));
        }

        return itemDtos;
    }

    public Request mapToRequest(Integer requestorId, CreateRequestDto createRequestDto) {
        return Request.builder()
                .description(createRequestDto.getDescription())
                .created(LocalDateTime.now())
                .requestor(userStorage.getUserById(requestorId))
                .build();
    }

    public List<RequestDto> mapToRequestsDto(List<Request> requests, Integer userId) {
        List<RequestDto> requestsDto = new ArrayList<>();
        for (Request request : requests) {
            requestsDto.add(mapToRequestsDto(request, userId));
        }
        return requestsDto;
    }
}
