package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.User;
import java.util.List;

public interface UserService {
    void update(User user);
    void delete(Long id);
    User getById(Long id);
    List<User> findAllUsers();
    void save(User user);
    User findByEmail(String email);
}
