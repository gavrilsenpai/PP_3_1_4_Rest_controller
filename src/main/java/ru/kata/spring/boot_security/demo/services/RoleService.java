package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.Role;

import java.util.List;

public interface RoleService {
    List<Role> findAll();
    List<Role> findByIds(List<Long> ids);
    Role findByName(String name);
    void saveRole(Role role);
}
