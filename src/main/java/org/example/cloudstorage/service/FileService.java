package org.example.cloudstorage.service;

import org.example.cloudstorage.domain.dto.FileDataDto;
import org.example.cloudstorage.domain.dto.FileDetailsDto;
import org.example.cloudstorage.domain.entity.File;
import org.example.cloudstorage.domain.entity.User;
import org.example.cloudstorage.domain.entity.UserStorage;
import org.example.cloudstorage.exception.*;
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

    public FileDetailsDto saveFile(MultipartFile mpFile, String ownerName) {
        User owner = userRepository.findUserByUserName(ownerName);
        File file = new File(owner, mpFile.getOriginalFilename(), mpFile.getContentType(), mpFile.getSize(), saveToFileSystem(mpFile, ownerName));
        File dbFile = fileRepository.save(file);
        userStorageRepository.save(new UserStorage(owner, dbFile));
        return fileDetailsDtoMapper.apply(dbFile, userRepository.findShareUsersByFileId(dbFile.getId()));
    }

    public FileDataDto readFile(Long fileId, String ownerName) {
        File file = fileRepository.findById(fileId).orElseThrow(FileNotFoundException::new);
        if (ownerName.equals(file.getOwner().getName())) {
            byte[] bytes = readFromFileSystem(file.getPath());
            return new FileDataDto(file.getName(), bytes);
        } else {
            throw new UserNotAuthorizedException();
        }
    }

    public Page<FileDetailsDto> findUserFiles(String userName, String ownerName, String fileType, Pageable pageable) {
        if (!File.hasValidSortField(pageable.getSort())) {
            throw new InvalidSortParameterException();
        }
        List<FileDetailsDto> mappedFiles = new ArrayList<>();
        Page<File> page = fileRepository.findFilesByUserName(userName, ownerName, fileType, pageable);
        List<File> files = page.getContent();
        return page.map(file -> fileDetailsDtoMapper.apply(file, userRepository.findShareUsersByFileId(file.getId())));
    }

    public void deleteFile(Long fileId, String ownerName) {
        File file = fileRepository.findById(fileId).orElseThrow(FileNotFoundException::new);
        if (ownerName.equals(file.getOwner().getName())) {
            if (deleteFileFromFileSystem(file.getPath())) {
                fileRepository.deleteById(fileId);
            }
        } else {
            throw new UserNotAuthorizedException();
        }
    }

    public FileDetailsDto shareWithUser(Long fileId, String userName, String ownerName) {
        File file = fileRepository.findById(fileId).orElseThrow(FileNotFoundException::new);
        if (ownerName.equals(file.getOwner().getName())) {
            User user = userRepository.findUserByUserName(userName);
            if (user == null) {
                throw new UserNotFoundException();
            }
            userStorageRepository.save(new UserStorage(user, file));
            return fileDetailsDtoMapper.apply(file, userRepository.findShareUsersByFileId(fileId));
        } else {
            throw new UserNotAuthorizedException();
        }
    }

    public FileDetailsDto unshareWithUser(Long fileId, String userName, String ownerName) {
        File file = fileRepository.findById(fileId).orElseThrow(FileNotFoundException::new);
        if (ownerName.equals(file.getOwner().getName())) {
            User user = userRepository.findUserByUserName(userName);
            if (user == null) {
                throw new UserNotFoundException();
            }
            userStorageRepository.delete(new UserStorage(user, file));
            return fileDetailsDtoMapper.apply(file, userRepository.findShareUsersByFileId(fileId));
        } else {
            throw new UserNotAuthorizedException();
        }
    }

    private String saveToFileSystem(MultipartFile mpFile, String userName) {
        String parent = "%s/%s".formatted(storageLocation, userName);
        String path = "%s/%s".formatted(parent, mpFile.getOriginalFilename());
        try {
            Files.createDirectories(Path.of(parent));
        } catch (IOException e) {
            throw new FileNotWrittenException("Could not create the user directory.");
        }
        try (FileOutputStream outputStream = new FileOutputStream(path)) {
            outputStream.write(mpFile.getBytes());
        } catch (IOException e) {
            throw new FileNotWrittenException();
        }
        return path;
    }

    private byte[] readFromFileSystem(String path) {
        try (FileInputStream inputStream = new FileInputStream(path)) {
            return inputStream.readAllBytes();
        } catch (IOException e) {
            throw new FileNotReadException();
        }
    }

    private boolean deleteFileFromFileSystem(String path) {
        java.io.File file = new java.io.File(path);
        return file.delete();
    }
}
