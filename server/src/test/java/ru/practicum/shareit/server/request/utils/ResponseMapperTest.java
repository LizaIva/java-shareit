package ru.practicum.shareit.server.request.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.request.dto.ResponseDto;
import ru.practicum.shareit.server.request.model.Request;
import ru.practicum.shareit.server.request.model.Response;
import ru.practicum.shareit.server.request.utils.ResponseMapper;
import ru.practicum.shareit.server.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class ResponseMapperTest {

    @InjectMocks
    private ResponseMapper responseMapper;

    @Test
    void mapToResponsesDto() {
        User user1 = User.builder()
                .id(1)
                .build();
        Item item1 = Item.builder()
                .id(12)
                .name("1234")
                .build();
        Request request1 = Request.builder()
                .requestId(13)
                .build();

        Response response1 = Response.builder()
                .responseId(14)
                .owner(user1)
                .item(item1)
                .request(request1)
                .build();

        List<ResponseDto> actual = responseMapper.mapToResponsesDto(List.of(response1));

        assertNotNull(actual);
        assertEquals(1, actual.size());

        ResponseDto actualResponse = actual.get(0);

        assertEquals(user1.getId(), actualResponse.getOwnerId());
        assertEquals(request1.getRequestId(), actualResponse.getRequestId());
        assertEquals(item1.getId(), actualResponse.getItemId());
        assertEquals(item1.getName(), actualResponse.getName());
    }
}