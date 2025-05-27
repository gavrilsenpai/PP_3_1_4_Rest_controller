package ru.kata.spring.boot_security.demo.inits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.Set;

@Component
public class DataInit implements CommandLineRunner {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInit(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        try {
            if (roleService.findAll().isEmpty()) {
                roleService.saveRole(new Role("ROLE_USER"));
                roleService.saveRole(new Role("ROLE_ADMIN"));
            }
        } catch (Exception e) {
            System.err.println("Ошибка при создании ролей: " + e.getMessage());
            e.printStackTrace();
            return;
        }

        try {
            if (userService.findByEmail("admin@mail.ru") == null) {
                Role adminRole = roleService.findByName("ROLE_ADMIN");
                Role userRole = roleService.findByName("ROLE_USER");

                if (adminRole == null || userRole == null) {
                    throw new IllegalStateException("Роли ADMIN или USER не найдены");
                }

                User admin = new User();
                admin.setEmail("admin@mail.ru");
                admin.setPassword(passwordEncoder.encode("admin"));
                admin.setAge(26);
                admin.setRoles(Set.of(adminRole, userRole));
                userService.save(admin);
            }
        } catch (Exception e) {
            System.err.println("Ошибка при создании администратора: " + e.getMessage());
            e.printStackTrace();
        }

        try {
            if (userService.findByEmail("user@mail.ru") == null) {
                Role userRole = roleService.findByName("ROLE_USER");

                if (userRole == null) {
                    throw new IllegalStateException("Роль USER не найдена");
                }

                User user = new User();
                user.setEmail("user@mail.ru");
                user.setPassword(passwordEncoder.encode("user"));
                user.setAge(30);
                user.setRoles(Set.of(userRole));
                userService.save(user);
            }
        } catch (Exception e) {
            System.err.println("Ошибка при создании пользователя: " + e.getMessage());
            e.printStackTrace();
        }
    }
}