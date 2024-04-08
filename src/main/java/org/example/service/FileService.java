package org.example.service;

import org.example.domain.dto.FileDataDto;
import org.example.domain.dto.FileDetailsDto;
import org.example.domain.entity.File;
import org.example.domain.entity.User;
import org.example.domain.entity.UserStorage;
import org.example.mapper.FileDetailsDtoMapper;
import org.example.repository.FileRepository;
import org.example.repository.UserRepository;
import org.example.repository.UserStorageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {
    private FileRepository fileRepository;
    private UserRepository userRepository;
    private UserStorageRepository userStorageRepository;
    private FileDetailsDtoMapper fileDetailsDtoMapper;

    public FileService(FileRepository fileRepository, UserRepository userRepository, UserStorageRepository userStorageRepository, FileDetailsDtoMapper fileDetailsDtoMapper) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
        this.userStorageRepository = userStorageRepository;
        this.fileDetailsDtoMapper = fileDetailsDtoMapper;
    }

    public FileDetailsDto saveFile(MultipartFile mpFile, String userName) {
        User owner = userRepository.findUserByUserName(userName);
        File file = new File(owner, mpFile.getOriginalFilename(), mpFile.getContentType(), mpFile.getSize());
        File dbFile = fileRepository.save(file);
        userStorageRepository.save(new UserStorage(owner, dbFile));
        return fileDetailsDtoMapper.apply(dbFile, userRepository.findShareUsersByFileId(dbFile.getId()));
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

    public List<FileDetailsDto> findUserFiles(String userName) {
        List<FileDetailsDto> r = new ArrayList<>();
        List<File> files = fileRepository.findFilesByUserName(userName);
        for (File file : files) {
            r.add(fileDetailsDtoMapper.apply(file, userRepository.findShareUsersByFileId(file.getId())));
        }
        return r;
    }

    public void deleteFile(Long fileId, String userName) {
        if (userName.equals(fileRepository.findById(fileId).get().getOwner().getName())) {
            fileRepository.deleteById(fileId);
        } else {
            throw new RuntimeException("Wrong authentication!");
        }
    }

    public FileDetailsDto shareWithUser(Long fileId, String userName, String ownerName) {
        File file = fileRepository.findById(fileId).get();
        if (ownerName.equals(file.getOwner().getName())) {
            User user = userRepository.findUserByUserName(userName);
            userStorageRepository.save(new UserStorage(user, file));
            return fileDetailsDtoMapper.apply(file, userRepository.findShareUsersByFileId(fileId));
        } else {
            throw new RuntimeException("Wrong authentication!");
        }
    }

    public FileDetailsDto unshareWithUser(Long fileId, String userName, String ownerName) {
        File file = fileRepository.findById(fileId).get();
        if (ownerName.equals(file.getOwner().getName())) {
            User user = userRepository.findUserByUserName(userName);
            userStorageRepository.delete(new UserStorage(user, file));
            return fileDetailsDtoMapper.apply(file, userRepository.findShareUsersByFileId(fileId));
        } else {
            throw new RuntimeException("Wrong authentication!");
        }
    }
}
