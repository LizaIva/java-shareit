package ru.practicum.shareit.user.dto;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.validation.constraints.Email;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class UpdateUserDto {

    private String name;

    @Email(message = "Введнный email не правильного формата")
    private String email;
}
