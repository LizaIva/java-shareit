package ru.practicum.shareit.feedback.storage;

import ru.practicum.shareit.feedback.model.Feedback;

public interface FeedbackStorage {
    Feedback put(Feedback feedback);

    Feedback get(int feedbackId);

    void checkFeedback(int id);
}
