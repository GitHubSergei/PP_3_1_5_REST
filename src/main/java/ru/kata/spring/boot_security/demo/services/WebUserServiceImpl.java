package ru.kata.spring.boot_security.demo.services;

import org.springframework.data.jpa.repository.Query;
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

@Service
public class WebUserServiceImpl implements WebUserService {

    private final WebUserRepository webUserRepository;

    public WebUserServiceImpl(WebUserRepository webUserRepository) {
        this.webUserRepository = webUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String webusername) throws UsernameNotFoundException {
        Optional<WebUser> webUser = webUserRepository.findByUserlogin(webusername);

        if (webUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found!");
        }
        return webUser.get();
    }

    public WebUser findUserById(Long userId) {
        Optional<WebUser> userFromDb = webUserRepository.findById(userId);
        return userFromDb.orElse(new WebUser());
    }

    public List<WebUser> allUsers() {
        return webUserRepository.findAll();
    }

    public void saveUser(WebUser user) {
        user.setRoles(user.getRoles());
        user.setPassword(user.getPassword());
        webUserRepository.save(user);
    }

    public void updateUser(long id, WebUser user) {
        user.setId(id);
        webUserRepository.save(user);
    }

    public void deleteUser(Long userId) {
        if (webUserRepository.findById(userId).isPresent()) {
            webUserRepository.deleteById(userId);
        }
    }
}
