package ru.practicum.shareit.server.user.dto;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class UpdateUserDto {

    private String name;

    private String email;
}
