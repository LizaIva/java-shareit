package ru.practicum.shareit.feedback.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Feedback {

    private Integer id;
    private Integer userId;
    private Integer itemId;
    private Integer bookingId;
    private String comment;
}
