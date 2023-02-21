package ru.practicum.shareit.feedback.model;

import lombok.Data;


@Data
public class Feedback {
    Integer id;
    Integer userId;
    Integer itemId;
    Integer bookingId;
    String comment;
}
