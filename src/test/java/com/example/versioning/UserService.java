package com.example.versioning.client;

import java.util.List;

import com.example.versioning.model.User;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/api/users")
public interface UserService {

    // Version 1.0
    @GetExchange(version = "1.0")
    List<User> getUsers();

    // Version 1.1+
    @GetExchange(version = "1.1")
    List<User> getUsersV1_1();

    // Version 2.0+
    @GetExchange(version = "2.0")
    List<User> getUsersV2();

    // Get user by ID - Version 1.0
    @GetExchange(url = "/{id}", version = "1.0")
    User getUserById(@PathVariable Long id);

    // Get user by ID - Version 2.0+
    @GetExchange(url = "/{id}", version = "2.0")
    User getUserByIdV2(@PathVariable Long id);

    // Create user - Version 1.0
    @PostExchange(version = "1.0")
    User createUser(@RequestBody User user);

    // Create user - Version 2.0+
    @PostExchange(version = "2.0")
    User createUserV2(@RequestBody User user);
}
