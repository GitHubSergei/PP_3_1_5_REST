package ru.kata.spring.boot_security.demo.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String webusername) throws UsernameNotFoundException {
        Optional<User> webUser = userRepository.findByUserEmail(webusername);

        if (webUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found!");
        }
        return webUser.get();
    }

    public User findUserById(Long userId) {
        Optional<User> userFromDb = userRepository.findById(userId);
        return userFromDb.orElse(new User());
    }

    @Override
    public User findUserByEmail(String email) {
        Optional<User> webUser = userRepository.findByUserEmail(email);
        return webUser.orElse(new User());
    }

    public List<User> allUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void saveUser(User user) {

        Set<Role> rolesSet = new HashSet<>(user.getRoles());
        user.setRoles(new HashSet<>());
        for (Role role : rolesSet
             ) {
            user.addRole(roleRepository.getById(role.getId()));
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public void updateUser(long id, User user) {
        user.setId(id);
        if (user.getPassword().isEmpty()) {
            user.setPassword(userRepository.findById(id).get().getPassword());
        }
        saveUser(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        if (userRepository.findById(userId).isPresent()) {
            userRepository.deleteById(userId);
        }
    }

    @Override
    public List<String> getUserRoleNames(Long userId) {
        Optional<User> webUser = userRepository.findById(userId);
        if (!webUser.isEmpty()) {
            return webUser.get().getRoles().stream()
                    .map(Role::getRole)
                    .map(r->r.replaceAll("ROLE_",r))
                    .collect(Collectors.toList());
        }
        return List.of("");
    }
}
