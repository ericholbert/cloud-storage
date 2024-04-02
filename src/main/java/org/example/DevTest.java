package org.example;

import org.example.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DevTest implements CommandLineRunner {
    @Autowired private FileRepository fileRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(">>> DEV TEST");
    }
}
