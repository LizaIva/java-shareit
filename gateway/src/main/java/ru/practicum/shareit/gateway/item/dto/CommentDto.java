package ru.practicum.shareit.gateway.item.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class CommentDto {

    private Integer id;
    private String text;
    private String authorName;
    private LocalDateTime created;
}