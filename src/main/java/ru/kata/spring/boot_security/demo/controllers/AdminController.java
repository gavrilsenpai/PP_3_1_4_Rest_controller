package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping
    public String adminPage(Model model, Principal principal) {
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.findAll());
        model.addAttribute("currentUser", userService.findByEmail(principal.getName()));
        return "admin";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute("user") User user,
                             @RequestParam(value = "roleIds", required = false) List<Long> roleIds) {
        if (roleIds == null) {
            user.setRoles(new HashSet<>()); // Или установите дефолтную роль
        } else {
            user.setRoles(new HashSet<>(roleService.findByIds(roleIds)));
        }
        userService.save(user);
        return "redirect:/admin";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestParam(value = "roleIds", required = false) List<Long> roleIds) {
        if (roleIds == null) {
            User existingUser = userService.getById(user.getId());
            user.setRoles(existingUser.getRoles());
        } else {
            user.setRoles(new HashSet<>(roleService.findByIds(roleIds)));
        }
        userService.update(user);
        return "redirect:/admin";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
