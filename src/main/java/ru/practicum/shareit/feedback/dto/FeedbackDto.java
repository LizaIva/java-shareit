package ru.practicum.shareit.feedback.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Builder
@Data
public class FeedbackDto {

    private Integer id;

    @NotNull
    private Integer userId;

    @NotNull
    private Integer itemId;

    @NotNull
    private Integer bookingId;

    private String comment;
}
