package ru.practicum.shareit.comment.service;

import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CreateCommentDto;

public interface CommentService {
    CommentDto put(Integer itemId, Integer userId, CreateCommentDto commentDto);

    CommentDto getFeedbackById(Integer feedbackId);

}
