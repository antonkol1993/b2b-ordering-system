package com.yourapp.user.controller;

import com.yourapp.user.entity.User;
import com.yourapp.user.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/by-email")
    public User getByEmail(@RequestParam String email) {
        return userService.findByEmail(email);
    }
}