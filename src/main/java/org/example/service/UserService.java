package org.example.service;

import org.example.domain.dto.PublicUserDto;
import org.example.domain.entity.User;
import org.example.mapper.PublicUserDtoMapper;
import org.example.repository.UserRepository;
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
        User dbUser = new User();
        dbUser.setName(user.getName());
        dbUser.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(dbUser);
        return publicUserDtoMapper.apply(dbUser);
    }
}
