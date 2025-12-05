package com.example.versioning.controller;

import java.util.Arrays;
import java.util.List;

import com.example.versioning.model.User;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    // Version 1.0 - Basic version
    // Returns basic user information without fullName
    @GetMapping(version = "1.0")
    public List<User> getUsersV1() {
        return Arrays.asList(
                new User(1L, "john_doe", "john@example.com"),
                new User(2L, "jane_smith", "jane@example.com"));
    }

    // Version 1.1+ - Added pagination support
    @GetMapping(version = "1.1+")
    public List<User> getUsersV1_1(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Arrays.asList(
                new User(1L, "john_doe", "john@example.com"),
                new User(2L, "jane_smith", "jane@example.com"));
    }

    // Version 2.0+ - Latest
    // Returns full user information including fullName
    @GetMapping(version = "2.0+")
    public List<User> getUsersV2(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return Arrays.asList(
                new User(1L, "john_doe", "john@example.com", "John Doe"),
                new User(2L, "jane_smith", "jane@example.com", "Jane Smith"));
    }

    // Get single user by ID - Version 1.0
    @GetMapping(path = "/{id}", version = "1.0")
    public User getUserByIdV1(@PathVariable Long id) {
        return new User(id, "john_doe", "john@example.com");
    }

    // Get single user by ID - Version 2.0+
    @GetMapping(path = "/{id}", version = "2.0+")
    public User getUserByIdV2(@PathVariable Long id) {
        return new User(id, "john_doe", "john@example.com", "John Doe");
    }

    // Create user - Version 1.0
    @PostMapping(version = "1.0")
    public User createUserV1(@RequestBody User user) {
        user.setId(3L);
        return user;
    }

    // Create user - Version 2.0+
    @PostMapping(version = "2.0+")
    public User createUserV2(@RequestBody User user) {
        user.setId(3L);
        return user;
    }
}
