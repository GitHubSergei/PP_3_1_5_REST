package ru.kata.spring.boot_security.demo.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.WebUserDtoIn;
import ru.kata.spring.boot_security.demo.dto.WebUserDtoOut;
import ru.kata.spring.boot_security.demo.models.WebUser;
import ru.kata.spring.boot_security.demo.services.WebUserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class RestDataController {

    private final ModelMapper modelMapper;
    private final WebUserService webUserService;

    @Autowired
    public RestDataController(ModelMapper modelMapper, WebUserService webUserService) {
        this.modelMapper = modelMapper;
        this.webUserService = webUserService;
    }

    @GetMapping("/secured")
    public List<WebUserDtoOut> getWebUserList() {
        return webUserService.allUsers().stream().map(this::convertToWebUserDtoOut).collect(Collectors.toList());
    }

    @GetMapping("/all/whois")
    public WebUserDtoOut getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        WebUser webUser = webUserService.findUserByEmail(auth.getName());
        return convertToWebUserDtoOut(webUserService.findUserByEmail(auth.getName()));
    }

    @GetMapping("/secured/whois/{id}")
    public WebUserDtoOut getCurrentUserById(@PathVariable("id") long id) {
        return convertToWebUserDtoOut(webUserService.findUserById(id));
    }

    @PostMapping("/secured/new")
    public ResponseEntity<HttpStatus> create(@RequestBody WebUserDtoIn webUserDtoIn) {
        WebUser webUser = convertToWebUser(webUserDtoIn);
        webUserService.saveUser(webUser);
        return new ResponseEntity<HttpStatus>(HttpStatus.CREATED);
    }

    @DeleteMapping("/secured/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") long id) {
        webUserService.deleteUser(id);
        return new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED);
    }

    @PatchMapping("/secured/{id}")
    public ResponseEntity<HttpStatus> editUser(@PathVariable("id") long id, @RequestBody WebUserDtoIn webUserDtoIn) {
        WebUser webUser = convertToWebUser(webUserDtoIn);
        webUserService.updateUser(id, webUser);
        return new ResponseEntity<HttpStatus>(HttpStatus.OK);
    }

    private WebUser convertToWebUser(WebUserDtoIn webUserDtoIn) {
        WebUser webUser = new WebUser();
        return modelMapper.map(webUserDtoIn, WebUser.class);
    }

    private WebUserDtoOut convertToWebUserDtoOut(WebUser webUser) {
        return modelMapper.map(webUser, WebUserDtoOut.class);
    }
}
