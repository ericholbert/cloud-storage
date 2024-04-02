package org.example;

import org.example.repository.FileRepository;
import org.example.repository.UserRepository;
import org.example.repository.UserStorageRepository;
import org.example.service.FileService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DevTest implements CommandLineRunner {
    @Autowired private FileService fileService;
    @Autowired private UserService userService;
    @Autowired private FileRepository fileRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private UserStorageRepository userStorageRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>> DEV TEST");
    }
}
