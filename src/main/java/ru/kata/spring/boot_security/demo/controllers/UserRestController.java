package ru.kata.spring.boot_security.demo.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.UserDtoIn;
import ru.kata.spring.boot_security.demo.dto.UserDtoOut;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class UserRestController {

    private final ModelMapper modelMapper;
    private final UserService webUserService;

    @Autowired
    public UserRestController(ModelMapper modelMapper, UserService userService) {
        this.modelMapper = modelMapper;
        this.webUserService = userService;
    }

    @GetMapping("/user")
    public List<UserDtoOut> getWebUserList() {
        return webUserService.allUsers().stream().map(this::convertToWebUserDtoOut).collect(Collectors.toList());
    }

    @GetMapping("/all/whois")
    public UserDtoOut getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = webUserService.findUserByEmail(auth.getName());
        return convertToWebUserDtoOut(webUserService.findUserByEmail(auth.getName()));
    }

    @GetMapping("/user/{id}")
    public UserDtoOut getCurrentUserById(@PathVariable("id") long id) {
        return convertToWebUserDtoOut(webUserService.findUserById(id));
    }

    @PutMapping("/user")
    public ResponseEntity<HttpStatus> create(@RequestBody UserDtoIn userDtoIn) {
        User user = convertToWebUser(userDtoIn);
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
        User user = convertToWebUser(userDtoIn);
        webUserService.updateUser(id, user);
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }

    private User convertToWebUser(UserDtoIn userDtoIn) {
        User user = new User();
        return modelMapper.map(userDtoIn, User.class);
    }

    private UserDtoOut convertToWebUserDtoOut(User user) {
        return modelMapper.map(user, UserDtoOut.class);
    }
}
