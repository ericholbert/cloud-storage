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

    public ShareDetailsDto share(Long userId, Long fileId) {
        User user = userRepository.findById(userId).get();
        File file = fileRepository.findById(fileId).get();
        userStorageRepository.save(new UserStorage(user, file));
        List<User> shareUsers = userRepository.findShareUsersByFileId(fileId);
        return new ShareDetailsDto(file.getName(), file.getOwner(), shareUsers);
    }

    public ShareDetailsDto unshare(Long userId, Long fileId) {
        User user = userRepository.findById(userId).get();
        File file = fileRepository.findById(fileId).get();
        userStorageRepository.delete(new UserStorage(user, file));
        List<User> shareUsers = userRepository.findShareUsersByFileId(fileId);
        return new ShareDetailsDto(file.getName(), file.getOwner(), shareUsers);
    }

    public ShareDetailsDto find(Long fileId) {
        File file = fileRepository.findById(fileId).get();
        List<User> shareUsers = userRepository.findShareUsersByFileId(fileId);
        return new ShareDetailsDto(file.getName(), file.getOwner(), shareUsers);
    }
}
