package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.Status;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDateTime;

@Data
public class BookingDto {
    private Integer id;

    @PastOrPresent(message = "Время начала бронирования не может быть в будущем")
    @NotNull
    private LocalDateTime start;

    @NotNull
    private LocalDateTime end;

    @NotNull
    private Integer itemId;

    private Status status;
}


//  брать её в аренду на какое-то время.
//Сервис должен не только позволять бронировать вещь на определённые даты, но и закрывать
// к ней доступ на время бронирования от других желающих.

//
//После того как вещь возвращена, у пользователя, который её арендовал, должна быть возможность оставить отзыв.
// В отзыве можно поблагодарить владельца вещи и подтвердить, что задача выполнена??? - типо еще какое-то поле?


//  в классе отзывов сделать метод на создание отзыва, а внем метод с проверкой - endTime у букинга уже закончилось или еще нет?

