package ru.practicum.shareit.comment.storage;

import ru.practicum.shareit.comment.model.Comment;

public interface CommentStorage {
    String USER_NOT_BOOKER_OF_THIS_ITEM_MSG = "item с id = %s не был в аренде у пользователю с id = %s";

    String COMMENT_NOT_FOUND = "comment с id = %s не найден.";
    Comment put(Comment comment);

    Comment get(int commentId);

    void checkComment(int id);
}
