package ru.practicum.shareit.comment.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CreateCommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.service.CommentService;
import ru.practicum.shareit.comment.storage.CommentStorage;
import ru.practicum.shareit.comment.utils.CommentMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentStorage commentStorage;
    private final CommentMapper commentMapper;
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    @Override
    public CommentDto put(Integer itemId, Integer userId, CreateCommentDto commentDto) {
        log.info("Создание отзыва");
        Comment comment = commentMapper.mapToComment(itemId, userId, commentDto);

        Comment savedComment = commentStorage.put(comment);
        return commentMapper.mapToCommentDto(savedComment);
    }

    @Override
    public CommentDto getFeedbackById(Integer feedbackId) {
        log.info("Запрос отзыва с id = {}", feedbackId);
        commentStorage.checkComment(feedbackId);

        return commentMapper.mapToCommentDto(commentStorage.get(feedbackId));
    }
}
