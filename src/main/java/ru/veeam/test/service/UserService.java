package ru.veeam.test.service;

import ru.veeam.test.dto.UserDto;
import ru.veeam.test.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> createUser(UserDto user);
    Optional<User> updateUser(String username, UserDto user);
    List<User> getAll();
    Optional<User> findByUsername(String username);
    void deleteUser(String username);
}
