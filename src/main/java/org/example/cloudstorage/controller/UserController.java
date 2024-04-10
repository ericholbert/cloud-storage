package org.example.cloudstorage.controller;

import org.example.cloudstorage.domain.dto.PublicUserDto;
import org.example.cloudstorage.service.UserService;
import org.example.cloudstorage.domain.entity.User;
import org.example.cloudstorage.mapper.PublicUserDtoLinkMapper;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private UserService userService;
    private PublicUserDtoLinkMapper publicUserDtoLinkMapper;

    public UserController(UserService userService, PublicUserDtoLinkMapper publicUserDtoLinkMapper) {
        this.userService = userService;
        this.publicUserDtoLinkMapper = publicUserDtoLinkMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<EntityModel<PublicUserDto>> register(@RequestBody User user) {
        PublicUserDto userDto = userService.registerUser(user);
        return ResponseEntity.ok(publicUserDtoLinkMapper.toModel(userDto));
    }
}
