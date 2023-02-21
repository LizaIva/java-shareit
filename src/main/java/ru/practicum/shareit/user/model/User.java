package ru.practicum.shareit.user.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class User {
    private Integer id;
    private String name;
    private String email;

    List<Integer> itemsIds = new ArrayList<>();
}
