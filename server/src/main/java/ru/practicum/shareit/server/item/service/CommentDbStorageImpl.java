package ru.practicum.shareit.server.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.server.booking.repository.BookingRepository;
import ru.practicum.shareit.server.exception.CheckBookerException;
import ru.practicum.shareit.server.item.model.Comment;
import ru.practicum.shareit.server.item.repository.CommentRepository;
import ru.practicum.shareit.server.item.storage.CommentStorage;
import ru.practicum.shareit.server.item.storage.ItemStorage;
import ru.practicum.shareit.server.user.storage.UserStorage;

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
}
