package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Comment;

public interface CommentStorage {
    String USER_NOT_BOOKER_OF_THIS_ITEM_MSG = "item с id = %s не был в аренде у пользователю с id = %s";

    Comment put(Comment comment);
}
