package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserDto {

    private Integer id;

    private String name;

    private String email;
}
