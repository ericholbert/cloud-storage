package org.example.cloudstorage.service;

import org.example.cloudstorage.domain.dto.PublicUserDto;
import org.example.cloudstorage.exception.InvalidUserMatchException;
import org.example.cloudstorage.exception.InvalidUserRequestBodyException;
import org.example.cloudstorage.mapper.PublicUserDtoMapper;
import org.example.cloudstorage.repository.UserRepository;
import org.example.cloudstorage.domain.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private PublicUserDtoMapper publicUserDtoMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, PublicUserDtoMapper publicUserDtoMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.publicUserDtoMapper = publicUserDtoMapper;
    }

    public PublicUserDto registerUser(User user) {
        if (user.getName() == null) {
            throw new InvalidUserRequestBodyException("The request body is missing the 'name' property.");
        }
        if (userRepository.findUserByUserName(user.getName()) != null) {
            throw new InvalidUserMatchException("The user already exists.");
        }
        if (user.getPassword() == null) {
            throw new InvalidUserRequestBodyException("The request body is missing the 'password' property.");
        }
        User dbUser = new User();
        dbUser.setName(user.getName());
        dbUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(dbUser);
        return publicUserDtoMapper.apply(dbUser);
    }
}
