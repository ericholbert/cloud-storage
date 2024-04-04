package org.example.controller;

import org.example.domain.dto.PublicUserDto;
import org.example.domain.entity.User;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    ResponseEntity<PublicUserDto> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }
}
