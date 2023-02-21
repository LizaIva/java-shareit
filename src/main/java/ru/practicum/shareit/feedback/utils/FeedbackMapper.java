package ru.practicum.shareit.feedback.utils;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.feedback.dto.FeedbackDto;
import ru.practicum.shareit.feedback.model.Feedback;


@Component
public class FeedbackMapper {
    public FeedbackDto mapToFeedbackDto(Feedback feedback) {
        FeedbackDto feedbackDto = new FeedbackDto();
        feedbackDto.setId(feedback.getId());
        feedbackDto.setItemId(feedback.getItemId());
        feedbackDto.setUserId(feedback.getUserId());
        feedbackDto.setBookingId(feedback.getBookingId());
        feedbackDto.setComment(feedback.getComment());
        return feedbackDto;
    }

    public Feedback mapToFeedback(FeedbackDto feedbackDto) {
        Feedback feedback = new Feedback();
        feedback.setId(feedbackDto.getId());
        feedback.setItemId(feedbackDto.getItemId());
        feedback.setUserId(feedbackDto.getUserId());
        feedback.setBookingId(feedbackDto.getBookingId());
        feedback.setComment(feedbackDto.getComment());
        return feedback;
    }
}
