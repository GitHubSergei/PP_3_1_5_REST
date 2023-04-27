package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.WebUser;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.WebUserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;


public interface WebUserService extends UserDetailsService {


    @Override
    UserDetails loadUserByUsername(String webusername);

    WebUser findUserById(Long userId);

    List<WebUser> allUsers();

    void saveUser(WebUser user);

    void updateUser(long id, WebUser user);

    void deleteUser(Long userId);

    List<String> getUserRoleNames(Long id);
}
