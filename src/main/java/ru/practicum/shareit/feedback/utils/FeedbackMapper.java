package ru.practicum.shareit.feedback.utils;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.feedback.dto.FeedbackDto;
import ru.practicum.shareit.feedback.model.Feedback;


@Component
public class FeedbackMapper {
    public FeedbackDto mapToFeedbackDto(Feedback feedback) {
        return FeedbackDto.builder()
                .id(feedback.getId())
                .itemId(feedback.getItemId())
                .userId(feedback.getUserId())
                .bookingId(feedback.getBookingId())
                .comment(feedback.getComment())
                .build();
    }

    public Feedback mapToFeedback(FeedbackDto feedbackDto) {
        return Feedback.builder()
                .id(feedbackDto.getId())
                .itemId(feedbackDto.getItemId())
                .userId(feedbackDto.getUserId())
                .bookingId(feedbackDto.getBookingId())
                .comment(feedbackDto.getComment())
                .build();
    }
}
