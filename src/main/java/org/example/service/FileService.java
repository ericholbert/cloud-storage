package org.example.service;

import org.example.domain.dto.FileDetailsDto;
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

    public File saveFile(MultipartFile mpFile, Long ownerId) {
        User owner = userRepository.findById(ownerId).get();
        File file = new File(owner, mpFile.getOriginalFilename());
        File dbFile = fileRepository.save(file);
        userStorageRepository.save(new UserStorage(owner, dbFile));
        return dbFile;
    }

    public FileDetailsDto readFile(Long id) throws IOException {
        // TODO: The file object should contain a path to the file in the file system
        File file = fileRepository.findById(id).get();
        byte[] bytes = Files.readAllBytes(Path.of("src/main/resources/dev_test/pic.jpg"));
        return new FileDetailsDto(file.getName(), bytes);
    }

    public List<File> findFilesByUserId(Long userId) {
        return fileRepository.findFilesByUserId(userId);
    }

    public void delete(Long fileId) {
        fileRepository.deleteById(fileId);
    }
}
