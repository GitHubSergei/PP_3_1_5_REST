package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.WebUser;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.WebUserService;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final WebUserService userService;

    private final RoleService roleService;

    public AdminController(WebUserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/adminpanel")
    public String userList(Model model) {
        List<WebUser> usr = userService.allUsers();
        WebUser newUser = new WebUser();
        List<Role> listOfRoles = roleService.getAllRoles();
        Set<Role> emptyRoles = new LinkedHashSet<>();
        newUser.setRoles(emptyRoles);

        model.addAttribute("allUsers", usr);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("user", userService.loadUserByUsername(auth.getName()));
        model.addAttribute("listroles", listOfRoles);
        model.addAttribute("newUser", newUser);
        return "adminpanel";
    }

    @PostMapping
    public String addUser(@ModelAttribute("newUser") WebUser newUser) {
        System.out.println(newUser.getName());
        userService.saveUser(newUser);
        return "redirect:admin/adminpanel";
    }

    @GetMapping("/{id}")
    public String editUser(Model model, @PathVariable("id") long id) {
        List<Role> listOfRoles = roleService.getAllRoles();
        model.addAttribute("updUser", userService.findUserById(id));
        model.addAttribute("listroles", listOfRoles);
        return "adminpanel";
    }

    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute("usr") WebUser user, @ModelAttribute("newUser") WebUser newUser,
                             @PathVariable("id") int id) {
        userService.updateUser(id, user);
        return "redirect:adminpanel";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return "redirect:adminpanel";
    }
}