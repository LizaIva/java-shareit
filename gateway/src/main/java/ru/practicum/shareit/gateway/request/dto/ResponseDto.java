package ru.practicum.shareit.gateway.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto {

    private Integer itemId;

    private String name;

    private Integer ownerId;

    private Integer requestId;
}