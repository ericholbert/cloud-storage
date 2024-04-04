package org.example.service;

import org.example.domain.dto.FileDataDto;
import org.example.domain.entity.File;
import org.example.domain.entity.User;
import org.example.domain.entity.UserStorage;
import org.example.repository.FileRepository;
import org.example.repository.UserRepository;
import org.example.repository.UserStorageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class FileService {
    private FileRepository fileRepository;
    private UserRepository userRepository;
    private UserStorageRepository userStorageRepository;

    public FileService(FileRepository fileRepository, UserRepository userRepository, UserStorageRepository userStorageRepository) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
        this.userStorageRepository = userStorageRepository;
    }

    public File saveFile(MultipartFile mpFile, String userName) {
        User owner = userRepository.findUserByUserName(userName);
        File file = new File(owner, mpFile.getOriginalFilename());
        File dbFile = fileRepository.save(file);
        userStorageRepository.save(new UserStorage(owner, dbFile));
        return dbFile;
    }

    public FileDataDto readFile(Long id, String userName) throws IOException {
        if (userName.equals(fileRepository.findById(id).get().getOwner().getName())) {
            // TODO: The file object should contain a path to the file in the file system
            File file = fileRepository.findById(id).get();
            byte[] bytes = Files.readAllBytes(Path.of("src/main/resources/dev_test/pic.jpg"));
            return new FileDataDto(file.getName(), bytes);
        } else {
            throw new RuntimeException("Wrong authentication!");
        }
    }

    public List<File> findUserFiles(String userName) {
        return fileRepository.findFilesByUserName(userName);
    }

    public void deleteFile(Long fileId, String userName) {
        if (userName.equals(fileRepository.findById(fileId).get().getOwner().getName())) {
            fileRepository.deleteById(fileId);
        } else {
            throw new RuntimeException("Wrong authentication!");
        }
    }
}
