package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.UserDtoIn;
import ru.kata.spring.boot_security.demo.dto.UserDtoOut;
import ru.kata.spring.boot_security.demo.mapper.UserMapper;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserRestController {


    private final UserService webUserService;
    private final UserMapper userMapper;

    @Autowired
    public UserRestController(UserService userService, UserMapper userMapper) {
        this.webUserService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/user")
    public List<UserDtoOut> getWebUserList() {
        return webUserService.allUsers().stream().map(userMapper::convertToUserDtoOut).collect(Collectors.toList());
    }

    @GetMapping("/all/whois")
    public UserDtoOut getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = webUserService.findUserByEmail(auth.getName());
        return userMapper.convertToUserDtoOut(webUserService.findUserByEmail(auth.getName()));
    }

    @GetMapping("/user/{id}")
    public UserDtoOut getCurrentUserById(@PathVariable("id") long id) {
        return userMapper.convertToUserDtoOut(webUserService.findUserById(id));
    }

    @PutMapping("/user")
    public ResponseEntity<HttpStatus> create(@RequestBody UserDtoIn userDtoIn) {
        User user = userMapper.convertToUser(userDtoIn);
        webUserService.saveUser(user);
        return new ResponseEntity<HttpStatus>(HttpStatus.CREATED);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") long id) {
        webUserService.deleteUser(id);
        return new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED);
    }

    @PostMapping("/user/{id}")
    public ResponseEntity<HttpStatus> editUser(@PathVariable("id") long id, @RequestBody UserDtoIn userDtoIn) {
        User user = userMapper.convertToUser(userDtoIn);
        webUserService.updateUser(id, user);
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }
}
