package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private WebUserService userService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/adminpanel")
    public String userList(Model model) {
        model.addAttribute("allUsers", userService.allUsers());
        return "adminpanel";
    }

    @GetMapping("/addnew")
    public String newUser(Model model) {
        WebUser user = new WebUser();

        List<Role> listOfRoles = roleService.getAllRoles();

        Set<Role> emptyRoles = new LinkedHashSet<>();
        user.setRoles(emptyRoles);
        model.addAttribute("listroles", listOfRoles);
        model.addAttribute("user", user);

        return "addnew";
    }

    @PostMapping
    public String addUser(@ModelAttribute("user") WebUser user) {
        userService.saveUser(user);
        return "redirect:admin/adminpanel";
    }

    @GetMapping("/{id}/edit")
    public String editUser(Model model, @PathVariable("id") long id) {
        List<Role> listOfRoles = roleService.getAllRoles();
        model.addAttribute("user", userService.findUserById(id));
        model.addAttribute("listroles", listOfRoles);
        return "/edit";
    }

    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute("user") WebUser user, @PathVariable("id") int id) {
        userService.updateUser(id, user);
        return "redirect:adminpanel";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.deleteUser(id);
        return "redirect:adminpanel";
    }
}