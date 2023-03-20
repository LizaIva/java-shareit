package ru.practicum.shareit.item.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.CheckBookerException;
import ru.practicum.shareit.exception.UnknownDataException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.storage.CommentStorage;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

@Component("commentDbStorageImpl")
@RequiredArgsConstructor
public class CommentDbStorageImpl implements CommentStorage {

    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;


    @Override
    public Comment put(Comment comment) {
        userStorage.checkUser(comment.getAuthor().getId());
        itemStorage.getItemById(comment.getItem().getId());

        if (!bookingRepository.isUserWasBooker(comment.getItem().getId(), comment.getAuthor().getId())) {
            throw new CheckBookerException(String.format(USER_NOT_BOOKER_OF_THIS_ITEM_MSG, comment.getItem().getId(),
                    comment.getAuthor().getId()));
        }

        return commentRepository.saveAndFlush(comment);
    }

    @Override
    public Comment get(int commentId) {
        return null;
    }

    @Override
    public void checkComment(int id) {
        if (commentRepository.existsById(id)) {
            throw new UnknownDataException(String.format(COMMENT_NOT_FOUND, id));
        }
    }
}
