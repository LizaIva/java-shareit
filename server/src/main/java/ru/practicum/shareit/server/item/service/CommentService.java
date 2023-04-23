package ru.practicum.shareit.server.item.service;

import ru.practicum.shareit.server.item.dto.CommentDto;
import ru.practicum.shareit.server.item.dto.CreateCommentDto;

public interface CommentService {
    CommentDto put(Integer itemId, Integer userId, CreateCommentDto commentDto);
}
