package ru.practicum.shareit.server.item.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.server.item.dto.CommentDto;
import ru.practicum.shareit.server.item.dto.CreateCommentDto;
import ru.practicum.shareit.server.item.model.Comment;
import ru.practicum.shareit.server.item.service.CommentService;
import ru.practicum.shareit.server.item.storage.CommentStorage;
import ru.practicum.shareit.server.item.utils.CommentMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentStorage commentStorage;
    private final CommentMapper commentMapper;

    @Override
    public CommentDto put(Integer itemId, Integer userId, CreateCommentDto commentDto) {
        log.info("Создание отзыва");
        Comment comment = commentMapper.mapToComment(itemId, userId, commentDto);

        Comment savedComment = commentStorage.put(comment);
        return commentMapper.mapToCommentDto(savedComment);
    }
}
