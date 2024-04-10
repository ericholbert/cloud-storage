package org.example.cloudstorage.service;

import org.example.cloudstorage.domain.dto.FileDataDto;
import org.example.cloudstorage.domain.dto.FileDetailsDto;
import org.example.cloudstorage.domain.entity.File;
import org.example.cloudstorage.domain.entity.User;
import org.example.cloudstorage.domain.entity.UserStorage;
import org.example.cloudstorage.mapper.FileDetailsDtoMapper;
import org.example.cloudstorage.mapper.PublicUserDtoMapper;
import org.example.cloudstorage.repository.FileRepository;
import org.example.cloudstorage.repository.UserRepository;
import org.example.cloudstorage.repository.UserStorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
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
    @Value("${custom.storage-location}")
    private String storageLocation;
    @Autowired
    PublicUserDtoMapper publicUserDtoMapper;

    public FileService(FileRepository fileRepository, UserRepository userRepository, UserStorageRepository userStorageRepository, FileDetailsDtoMapper fileDetailsDtoMapper) {
        this.fileRepository = fileRepository;
        this.userRepository = userRepository;
        this.userStorageRepository = userStorageRepository;
        this.fileDetailsDtoMapper = fileDetailsDtoMapper;
    }

    public FileDetailsDto saveFile(MultipartFile mpFile, String userName) throws IOException {
        User owner = userRepository.findUserByUserName(userName);
        File file = new File(owner, mpFile.getOriginalFilename(), mpFile.getContentType(), mpFile.getSize(), saveToFileSystem(mpFile, userName));
        File dbFile = fileRepository.save(file);
        userStorageRepository.save(new UserStorage(owner, dbFile));
        return fileDetailsDtoMapper.apply(dbFile, userRepository.findShareUsersByFileId(dbFile.getId()));
    }

    public FileDataDto readFile(Long id, String userName) throws IOException {
        if (userName.equals(fileRepository.findById(id).get().getOwner().getName())) {
            File file = fileRepository.findById(id).get();
            byte[] bytes = readFromFileSystem(file.getPath());
            return new FileDataDto(file.getName(), bytes);
        } else {
            throw new RuntimeException("Wrong authentication!");
        }
    }

    public Page<FileDetailsDto> findUserFiles(String userName, String ownerName, String fileType, Pageable pageable) {
        if (!File.hasValidSortField(pageable.getSort())) {
            throw new RuntimeException("Invalid sort field!");
        }
        List<FileDetailsDto> mappedFiles = new ArrayList<>();
        Page<File> page = fileRepository.findFilesByUserName(userName, ownerName, fileType, pageable);
        List<File> files = page.getContent();
        return page.map(file -> fileDetailsDtoMapper.apply(file, userRepository.findShareUsersByFileId(file.getId())));
    }

    public void deleteFile(Long fileId, String userName) {
        if (userName.equals(fileRepository.findById(fileId).get().getOwner().getName())) {
            File file = fileRepository.findById(fileId).get();
            if (deleteFileFromFileSystem(file.getPath())) {
                fileRepository.deleteById(fileId);
            }
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

    private String saveToFileSystem(MultipartFile mpFile, String userName) throws IOException {
        String parent = "%s/%s".formatted(storageLocation, userName);
        String path = "%s/%s".formatted(parent, mpFile.getOriginalFilename());
        Files.createDirectories(Path.of(parent));
        FileOutputStream outputStream = new FileOutputStream(path);
        outputStream.write(mpFile.getBytes());
        return path;
    }

    private byte[] readFromFileSystem(String path) throws IOException {
        FileInputStream inputStream = new FileInputStream(path);
        return inputStream.readAllBytes();
    }

    private boolean deleteFileFromFileSystem(String path) {
        java.io.File file = new java.io.File(path);
        return file.delete();
    }
}
