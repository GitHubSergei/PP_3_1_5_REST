package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.WebUser;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.WebUserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class WebUserServiceImpl implements WebUserService {

    private final WebUserRepository webUserRepository;
    private final RoleRepository roleRepository;
    public WebUserServiceImpl(WebUserRepository webUserRepository, RoleRepository roleRepository) {
        this.webUserRepository = webUserRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String webusername) throws UsernameNotFoundException {
        Optional<WebUser> webUser = webUserRepository.findByUserEmail(webusername);

        if (webUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found!");
        }
        return webUser.get();
    }

    public WebUser findUserById(Long userId) {
        Optional<WebUser> userFromDb = webUserRepository.findById(userId);
        return userFromDb.orElse(new WebUser());
    }

    @Override
    public WebUser findUserByEmail(String email) {
        Optional<WebUser> webUser = webUserRepository.findByUserEmail(email);
        return webUser.orElse(new WebUser());
    }

    public List<WebUser> allUsers() {
        return webUserRepository.findAll();
    }

    public void saveUser(WebUser user) {
        //user.setRoles(user.getRoles());
        Set<Role> rolesSet = new HashSet<>(user.getRoles());
        user.setRoles(new HashSet<>());
        for (Role role : rolesSet
             ) {
            user.addRole(roleRepository.getById(role.getId()));
        }
        user.setPassword(user.getPassword());
        webUserRepository.save(user);
    }

    public void updateUser(long id, WebUser user) {
        user.setId(id);
        if (user.getPassword().isEmpty()) {
            user.setPassword(webUserRepository.findById(id).get().getPassword());
        }
        saveUser(user);
    }

    public void deleteUser(Long userId) {
        if (webUserRepository.findById(userId).isPresent()) {
            webUserRepository.deleteById(userId);
        }
    }


    @Override
    public List<String> getUserRoleNames(Long userId) {
        Optional<WebUser> webUser = webUserRepository.findById(userId);
        if (!webUser.isEmpty()) {
            return webUser.get().getRoles().stream()
                    .map(Role::getRole)
                    .map(r->r.replaceAll("ROLE_",r))
                    .collect(Collectors.toList());
        }
        return List.of("");
    }
}
