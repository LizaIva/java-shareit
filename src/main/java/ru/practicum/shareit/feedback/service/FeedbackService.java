package ru.practicum.shareit.feedback.service;

import ru.practicum.shareit.feedback.dto.FeedbackDto;

public interface FeedbackService {
    FeedbackDto put(FeedbackDto feedbackDto);

    FeedbackDto getFeedbackById(Integer feedbackId);

    void checkAccess(int bookingId);
}
