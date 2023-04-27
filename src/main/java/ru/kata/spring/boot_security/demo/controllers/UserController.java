package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.services.WebUserService;

@Controller
@RequestMapping("/userarea")
public class UserController {
    private final WebUserService userService;

    public UserController(WebUserService userService) {
        this.userService = userService;
    }


    @GetMapping("/user")
    public String userList(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("user", userService.loadUserByUsername(auth.getName()));
        return "user";
    }
}
