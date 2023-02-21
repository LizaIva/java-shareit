package ru.practicum.shareit.user.utils;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {
    public UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public User mapToUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }

    public UpdateUserDto mapToUpdateUserDto(User user) {
        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setName(user.getName());
        updateUserDto.setEmail(user.getEmail());
        return updateUserDto;
    }

    public User mapToUser(UpdateUserDto updateUserDto) {
        User user = new User();
        user.setName(updateUserDto.getName());
        user.setEmail(updateUserDto.getEmail());
        return user;
    }

    public List<UserDto> mapToUsersDto(List<User> users) {
        List<UserDto> usersDto = new ArrayList<>();
        for (User user : users) {
            usersDto.add(mapToUserDto(user));
        }
        return usersDto;
    }


}
