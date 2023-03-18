package ru.practicum.shareit.comment.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.dto.CreateCommentDto;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    public List<CommentDto> mapToCommentDto(List<Comment> comments) {
        if (comments == null) {
            return null;
        }

        List<CommentDto> commentDtos = new ArrayList<>();
        for (Comment comment : comments) {
            commentDtos.add(mapToCommentDto(comment));
        }

        return commentDtos;
    }

    public CommentDto mapToCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .created(comment.getCreated())
                .authorName(comment.getAuthor().getName())
                .build();
    }

    public Comment mapToComment(Integer itemId, Integer userId, CreateCommentDto commentDto) {
        return Comment.builder()
                .item(itemStorage.getItemById(itemId))
                .author(userStorage.getUserById(userId))
                .text(commentDto.getText())
                .created(LocalDateTime.now())
                .item(itemStorage.getItemById(itemId))
                .author(userStorage.getUserById(userId))
                .build();
    }
}
