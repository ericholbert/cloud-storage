package org.example;

import org.example.repository.FileRepository;
import org.example.repository.UserRepository;
import org.example.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DevTest implements CommandLineRunner {
    @Autowired private FileRepository fileRepository;
    @Autowired private FileService fileService;
    @Autowired private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>> DEV TEST");
        //System.out.println(fileRepository.findAll());
        //fileService.delete(20L);
        //userRepository.deleteById(1L);
        //System.out.println(fileRepository.findAll());
    }
}
