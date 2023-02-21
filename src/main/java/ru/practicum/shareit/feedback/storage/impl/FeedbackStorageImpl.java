package ru.practicum.shareit.feedback.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.UnknownDataException;
import ru.practicum.shareit.feedback.model.Feedback;
import ru.practicum.shareit.feedback.storage.FeedbackStorage;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FeedbackStorageImpl implements FeedbackStorage {
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    private final Map<Integer, Feedback> feedbacks = new HashMap<>();
    private final Map<Integer, List<Feedback>> itemIdFeedbacks = new HashMap<>();
    private final Map<Integer, List<Feedback>> usersIdFeedbacks = new HashMap<>();

    int counter = 0;

    @Override
    public Feedback put(Feedback feedback) {
        int itemId = feedback.getItemId();
        int userId = feedback.getUserId();

        itemStorage.checkItem(itemId);
        userStorage.checkUser(userId);

        if (feedback.getId() == null) {
            feedback.setId(++counter);
        }

        feedbacks.put(feedback.getId(), feedback);

        if (!itemIdFeedbacks.containsKey(itemId)) {
            itemIdFeedbacks.put(itemId, new ArrayList<>(List.of(feedback)));
        } else {
            List<Feedback> itemFeedbacks = itemIdFeedbacks.get(itemId);
            itemFeedbacks.add(feedback);
        }

        if (!usersIdFeedbacks.containsKey(userId)) {
            usersIdFeedbacks.put(userId, new ArrayList<>(List.of(feedback)));
        } else {
            List<Feedback> userFeedbacks = usersIdFeedbacks.get(userId);
            userFeedbacks.add(feedback);
        }
        return feedback;
    }

    @Override
    public Feedback get(int feedbackId) {
        checkFeedback(feedbackId);

        log.info("Feedback с id = {} не найден.", feedbackId);
        return feedbacks.get(feedbackId);
    }

    @Override
    public void checkFeedback(int id) {
        Feedback feedback = feedbacks.get(id);
        if (feedback == null) {
            log.info("Feedback с id = {} не найден.", id);
            throw new UnknownDataException("Feedback с id = " + id + " не найден.");
        }
    }
}


