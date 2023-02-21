package ru.practicum.shareit.feedback.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FeedbackDto {
    Integer id;

    @NotNull
    Integer userId;

    @NotNull
    Integer itemId;

    @NotNull
    Integer bookingId;

    String comment;
}
