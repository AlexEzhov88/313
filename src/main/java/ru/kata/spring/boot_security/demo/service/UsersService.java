package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UsersService {
    void saveUser (User user);

    List<User> getAllUsers();

    User getUserById(Integer id);

    User getUserByUsername(String userName);

    void updateUser(User user);

    void removeUserById(Integer id);
}
