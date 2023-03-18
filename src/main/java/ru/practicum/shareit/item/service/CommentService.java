package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CreateCommentDto;

public interface CommentService {
    CommentDto put(Integer itemId, Integer userId, CreateCommentDto commentDto);

    CommentDto getFeedbackById(Integer feedbackId);

}
