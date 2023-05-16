package ru.kata.spring.boot_security.demo.mapper;

import ru.kata.spring.boot_security.demo.dto.UserDtoIn;
import ru.kata.spring.boot_security.demo.dto.UserDtoOut;
import ru.kata.spring.boot_security.demo.models.User;

public interface UserMapper {
    User convertToUser(UserDtoIn userDtoIn);
    UserDtoOut convertToUserDtoOut(User user);
}
