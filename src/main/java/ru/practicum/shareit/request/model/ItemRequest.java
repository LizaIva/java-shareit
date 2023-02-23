package ru.practicum.shareit.request.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class ItemRequest {

    private Integer id;
    private String description;
    private Integer requestorId;
    private LocalDateTime created;
}
