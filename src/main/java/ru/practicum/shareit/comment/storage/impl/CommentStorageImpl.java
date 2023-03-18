//package ru.practicum.shareit.comment.storage.impl;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import ru.practicum.shareit.exception.UnknownDataException;
//import ru.practicum.shareit.comment.model.Comment;
//import ru.practicum.shareit.comment.storage.CommentStorage;
//import ru.practicum.shareit.item.storage.ItemStorage;
//import ru.practicum.shareit.user.storage.UserStorage;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class CommentStorageImpl implements CommentStorage {
//    private static final String FEEDBACK_NOT_FOUND_MSG = "Feedback с id = %s не найден.";
//
//    private final UserStorage userStorage;
//    private final ItemStorage itemStorage;
//
//    private final Map<Integer, Comment> feedbacks = new HashMap<>();
//    private final Map<Integer, List<Comment>> itemIdFeedbacks = new HashMap<>();
//    private final Map<Integer, List<Comment>> usersIdFeedbacks = new HashMap<>();
//
//    int counter = 0;
//
//    @Override
//    public Comment put(Comment comment) {
//        int itemId = comment.getItemId();
//        int userId = comment.getUserId();
//
//        itemStorage.checkItem(itemId);
//        userStorage.checkUser(userId);
//
//        if (comment.getId() == null) {
//            comment.setId(++counter);
//        }
//
//        feedbacks.put(comment.getId(), comment);
//
//        if (!itemIdFeedbacks.containsKey(itemId)) {
//            itemIdFeedbacks.put(itemId, new ArrayList<>(List.of(comment)));
//        } else {
//            List<Comment> itemComments = itemIdFeedbacks.get(itemId);
//            itemComments.add(comment);
//        }
//
//        if (!usersIdFeedbacks.containsKey(userId)) {
//            usersIdFeedbacks.put(userId, new ArrayList<>(List.of(comment)));
//        } else {
//            List<Comment> userComments = usersIdFeedbacks.get(userId);
//            userComments.add(comment);
//        }
//        return comment;
//    }
//
//    @Override
//    public Comment get(int feedbackId) {
//        checkFeedback(feedbackId);
//
//        log.info("Feedback с id = {} не найден.", feedbackId);
//        return feedbacks.get(feedbackId);
//    }
//
//    @Override
//    public void checkFeedback(int id) {
//        Comment comment = feedbacks.get(id);
//        if (comment == null) {
//            log.info("Feedback с id = {} не найден.", id);
//            throw new UnknownDataException(String.format(FEEDBACK_NOT_FOUND_MSG, id));
//        }
//    }
//}
//
//
