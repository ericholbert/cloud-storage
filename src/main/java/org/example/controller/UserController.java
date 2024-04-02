package org.example.controller;

import org.example.domain.dto.ShareDetailsDto;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/share/{userId}/{fileId}")
    ResponseEntity<ShareDetailsDto> share(@PathVariable Long userId, @PathVariable Long fileId) {
        return ResponseEntity.ok(userService.share(userId, fileId));
    }

    @DeleteMapping("/share/{userId}/{fileId}")
    ResponseEntity<ShareDetailsDto> unshare(@PathVariable Long userId, @PathVariable Long fileId) {
        return ResponseEntity.ok(userService.unshare(userId, fileId));
    }

    @GetMapping("/share/{fileId}")
    ResponseEntity<ShareDetailsDto> find(@PathVariable Long fileId) {
        return ResponseEntity.ok(userService.find(fileId));
    }
}
