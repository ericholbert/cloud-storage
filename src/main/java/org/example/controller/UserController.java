package org.example.controller;

import org.example.domain.dto.ShareDetailsDto;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/share/{fileId}/{userName}")
    ResponseEntity<ShareDetailsDto> share(@PathVariable Long fileId,@PathVariable String userName) {
        return ResponseEntity.ok(userService.shareWithUser(fileId, userName));
    }

    @DeleteMapping("/share/{fileId}/{userName}")
    ResponseEntity<ShareDetailsDto> unshare(@PathVariable Long fileId,@PathVariable String userName) {
        return ResponseEntity.ok(userService.unshareWithUser(fileId, userName));
    }

    @GetMapping("/share/{fileId}")
    ResponseEntity<ShareDetailsDto> find(@PathVariable Long fileId, Principal principal) {
        return ResponseEntity.ok(userService.findShareUsers(fileId, principal.getName()));
    }
}
