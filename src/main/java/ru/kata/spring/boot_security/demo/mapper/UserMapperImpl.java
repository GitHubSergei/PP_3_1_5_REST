package ru.kata.spring.boot_security.demo.mapper;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dto.UserDtoIn;
import ru.kata.spring.boot_security.demo.dto.UserDtoOut;
import ru.kata.spring.boot_security.demo.models.User;

@Component
public class UserMapperImpl implements UserMapper {

    private final org.modelmapper.ModelMapper modelMapper;

    public UserMapperImpl(org.modelmapper.ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    @Override
    public User convertToUser(UserDtoIn userDtoIn) {
        User user = new User();
        return modelMapper.map(userDtoIn, User.class);
    }

    @Override
    public UserDtoOut convertToUserDtoOut(User user) {
        return modelMapper.map(user, UserDtoOut.class);
    }
}
