package ru.practicum.shareit.request.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.utils.ItemMapper;
import ru.practicum.shareit.request.dto.ResponseDto;
import ru.practicum.shareit.request.model.Response;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.utils.UserMapper;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ResponseMapper {
    private final ItemService itemService;
    private final UserService userService;
    private final UserMapper userMapper;
    private final ItemMapper itemMapper;


    public ResponseDto mapToResponseDto(Response response) {
        return ResponseDto.builder()
                .itemId(response.getItem().getId())
                .name(response.getItem().getName())
                .ownerId(response.getOwner().getId())
                .requestId(response.getRequest().getRequestId())
                .build();
    }

    public Response mapToResponse(ResponseDto responseDto) {
        User user = userMapper.mapToUser(userService.get(responseDto.getOwnerId()));
        ItemDto itemDto = itemService.getItemById(responseDto.getItemId(), user.getId());
        Item item = itemMapper.mapToItem(user.getId(), itemDto);

        return Response.builder()
                .item(item)
                .owner(user)
                .request(item.getRequest())
                .build();
    }

    public List<Response> mapToResponses(List<ResponseDto> responsesDto) {
        List<Response> responses = new ArrayList<>();
        for (ResponseDto responseDto : responsesDto) {
            responses.add(mapToResponse(responseDto));
        }
        return responses;
    }

    public List<ResponseDto> mapToResponsesDto(List<Response> responses) {
        List<ResponseDto> responsesDto = new ArrayList<>();

        if(responses == null || responses.isEmpty()){
            return responsesDto;
        }

        for (Response response : responses) {
            responsesDto.add(mapToResponseDto(response));
        }
        return responsesDto;
    }
}
