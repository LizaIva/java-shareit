package ru.practicum.shareit.feedback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.feedback.dto.FeedbackDto;
import ru.practicum.shareit.feedback.model.Feedback;
import ru.practicum.shareit.feedback.storage.FeedbackStorage;
import ru.practicum.shareit.feedback.utils.FeedbackMapper;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackService {
    private final FeedbackStorage feedbackStorage;
    private final FeedbackMapper feedbackMapper;
    private final BookingStorage bookingStorage;

    public FeedbackDto put(FeedbackDto feedbackDto) {
        log.info("Создание отзыва");
        Feedback feedback = feedbackMapper.mapToFeedback(feedbackDto);
        checkAccess(feedback.getBookingId());
        Feedback savedFeedback = feedbackStorage.put(feedback);
        return feedbackMapper.mapToFeedbackDto(savedFeedback);
    }

    public FeedbackDto getFeedbackById(Integer feedbackId) {
        log.info("Запрос отзыва с id = {}", feedbackId);
        feedbackStorage.checkFeedback(feedbackId);

        return feedbackMapper.mapToFeedbackDto(feedbackStorage.get(feedbackId));
    }

    public void checkAccess(int bookingId) {
        bookingStorage.checkBooking(bookingId);

        if (bookingStorage.getBookingById(bookingId).getEnd().isAfter(LocalDateTime.now())) {
            throw new ValidationException("Невозможно создать отзыв до окончания бронирования");
        }
    }

}
