package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;


public interface UserService extends UserDetailsService {


    @Override
    UserDetails loadUserByUsername(String webusername);

    User findUserById(Long userId);

    User findUserByEmail(String email);

    List<User> allUsers();

    void saveUser(User user);

    void updateUser(long id, User user);

    void deleteUser(Long userId);

    List<String> getUserRoleNames(Long id);
}
