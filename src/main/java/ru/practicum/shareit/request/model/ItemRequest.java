package ru.practicum.shareit.request.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemRequest {
    private Integer id;

    private String description;

    private Integer requestorId;

    private LocalDateTime created;
}
