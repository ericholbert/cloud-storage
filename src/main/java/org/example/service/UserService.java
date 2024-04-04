package org.example.service;

import org.example.domain.dto.ShareDetailsDto;
import org.example.domain.entity.File;
import org.example.domain.entity.User;
import org.example.domain.entity.UserStorage;
import org.example.repository.FileRepository;
import org.example.repository.UserRepository;
import org.example.repository.UserStorageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private FileRepository fileRepository;
    private UserRepository userRepository;
    private UserStorageRepository userStorageRepository;

    public UserService(FileRepository fileRepository, UserRepository userRepository, UserStorageRepository userStorageRepository) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
        this.userStorageRepository = userStorageRepository;
    }

    public User registerUser(User user) {
        User dbUser = new User();
        dbUser.setName(user.getName());
        dbUser.setPassword(user.getPassword());
        return userRepository.save(dbUser);
    }

    public ShareDetailsDto shareWithUser(Long fileId, String userName) {
        File file = fileRepository.findById(fileId).get();
        User user = userRepository.findUserByUserName(userName);
        userStorageRepository.save(new UserStorage(user, file));
        List<User> shareUsers = userRepository.findShareUsersByFileId(fileId);
        return new ShareDetailsDto(file.getName(), file.getOwner(), shareUsers);
    }

    public ShareDetailsDto unshareWithUser(Long fileId, String userName) {
        File file = fileRepository.findById(fileId).get();
        User user = userRepository.findUserByUserName(userName);
        userStorageRepository.delete(new UserStorage(user, file));
        List<User> shareUsers = userRepository.findShareUsersByFileId(fileId);
        return new ShareDetailsDto(file.getName(), file.getOwner(), shareUsers);
    }

    public ShareDetailsDto findShareUsers(Long fileId, String ownerName) {
        if (ownerName.equals(fileRepository.findById(fileId).get().getOwner().getName())) {
            File file = fileRepository.findById(fileId).get();
            List<User> shareUsers = userRepository.findShareUsersByFileId(fileId);
            return new ShareDetailsDto(file.getName(), file.getOwner(), shareUsers);
        } else {
            throw new RuntimeException("Wrong authentication!");
        }
    }
}
