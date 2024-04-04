package org.example.service;

import org.example.domain.dto.PublicUserDto;
import org.example.domain.entity.File;
import org.example.domain.entity.User;
import org.example.domain.entity.UserStorage;
import org.example.mapper.PublicUserDtoMapper;
import org.example.repository.FileRepository;
import org.example.repository.UserRepository;
import org.example.repository.UserStorageRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private FileRepository fileRepository;
    private UserRepository userRepository;
    private UserStorageRepository userStorageRepository;
    private PasswordEncoder passwordEncoder;
    private PublicUserDtoMapper publicUserDtoMapper;

    public UserService(FileRepository fileRepository, UserRepository userRepository, UserStorageRepository userStorageRepository, PasswordEncoder passwordEncoder, PublicUserDtoMapper publicUserDtoMapper) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
        this.userStorageRepository = userStorageRepository;
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

    public List<PublicUserDto> shareWithUser(Long fileId, String userName) {
        File file = fileRepository.findById(fileId).get();
        User user = userRepository.findUserByUserName(userName);
        userStorageRepository.save(new UserStorage(user, file));
        List<User> shareUsers = userRepository.findShareUsersByFileId(fileId);
        return shareUsers.stream()
                .map(dbUser -> publicUserDtoMapper.apply(dbUser))
                .toList();
    }

    public List<PublicUserDto> unshareWithUser(Long fileId, String userName) {
        File file = fileRepository.findById(fileId).get();
        User user = userRepository.findUserByUserName(userName);
        userStorageRepository.delete(new UserStorage(user, file));
        List<User> shareUsers = userRepository.findShareUsersByFileId(fileId);
        return shareUsers.stream()
                .map(dbUser -> publicUserDtoMapper.apply(dbUser))
                .toList();
    }

    public List<PublicUserDto> findShareUsers(Long fileId, String ownerName) {
        if (ownerName.equals(fileRepository.findById(fileId).get().getOwner().getName())) {
            File file = fileRepository.findById(fileId).get();
            List<User> shareUsers = userRepository.findShareUsersByFileId(fileId);
            return shareUsers.stream()
                    .map(dbUser -> publicUserDtoMapper.apply(dbUser))
                    .toList();
        } else {
            throw new RuntimeException("Wrong authentication!");
        }
    }
}
