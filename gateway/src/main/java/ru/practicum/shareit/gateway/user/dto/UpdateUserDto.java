package ru.practicum.shareit.gateway.user.dto;

import lombok.*;

import javax.validation.constraints.Email;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {

    private String name;

    @Email(message = "Введнный email не правильного формата")
    private String email;
}