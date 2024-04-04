package org.example.controller;

import org.example.domain.dto.PublicUserDto;
import org.example.domain.entity.User;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    ResponseEntity<PublicUserDto> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @PostMapping("/share/{fileId}/{userName}")
    ResponseEntity<List<PublicUserDto>> share(@PathVariable Long fileId,@PathVariable String userName) {
        return ResponseEntity.ok(userService.shareWithUser(fileId, userName));
    }

    @DeleteMapping("/share/{fileId}/{userName}")
    ResponseEntity<List<PublicUserDto>> unshare(@PathVariable Long fileId,@PathVariable String userName) {
        return ResponseEntity.ok(userService.unshareWithUser(fileId, userName));
    }

    @GetMapping("/share/{fileId}")
    ResponseEntity<List<PublicUserDto>> find(@PathVariable Long fileId, Principal principal) {
        return ResponseEntity.ok(userService.findShareUsers(fileId, principal.getName()));
    }
}
