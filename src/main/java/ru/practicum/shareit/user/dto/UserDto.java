package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class UserDto {
    private Integer id;

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @NotEmpty(message = "Email не может быть пустым")
    @Email(message = "Введнный email не правильного формата")
    private String email;


    public UserDto(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public UserDto() {
    }
}
