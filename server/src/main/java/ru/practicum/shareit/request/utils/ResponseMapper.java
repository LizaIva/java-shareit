package ru.practicum.shareit.request.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.dto.ResponseDto;
import ru.practicum.shareit.request.model.Response;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ResponseMapper {

    public ResponseDto mapToResponseDto(Response response) {
        return ResponseDto.builder()
                .itemId(response.getItem().getId())
                .name(response.getItem().getName())
                .ownerId(response.getOwner().getId())
                .requestId(response.getRequest().getRequestId())
                .build();
    }

    public List<ResponseDto> mapToResponsesDto(List<Response> responses) {
        List<ResponseDto> responsesDto = new ArrayList<>();

        if (responses == null || responses.isEmpty()) {
            return responsesDto;
        }

        for (Response response : responses) {
            responsesDto.add(mapToResponseDto(response));
        }
        return responsesDto;
    }
}
