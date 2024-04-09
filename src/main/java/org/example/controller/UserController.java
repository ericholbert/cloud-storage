package org.example.controller;

import org.example.domain.dto.PublicUserDto;
import org.example.domain.entity.User;
import org.example.mapper.PublicUserDtoLinkMapper;
import org.example.service.UserService;
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
