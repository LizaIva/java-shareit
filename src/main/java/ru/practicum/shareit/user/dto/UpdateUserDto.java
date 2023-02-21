package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class UpdateUserDto {
    private String name;

    @Email(message = "Введнный email не правильного формата")
    private String email;
}
