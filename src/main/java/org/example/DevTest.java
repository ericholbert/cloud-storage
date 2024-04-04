package org.example;

import org.example.domain.entity.File;
import org.example.domain.entity.User;
import org.example.domain.entity.UserStorage;
import org.example.repository.FileRepository;
import org.example.repository.UserRepository;
import org.example.repository.UserStorageRepository;
import org.example.service.FileService;
import org.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
        initDb();
    }

    private void initDb() {
        // Passwords are 'password'
        User u1 = new User(1L, "user1", "$2a$10$I7PssiJfoPU58ceXTkDiauclM8hw6yVt6pBSVpn7IL56J5xIvnv9K");
        User u2 = new User(2L, "user2", "$2a$10$I7PssiJfoPU58ceXTkDiauclM8hw6yVt6pBSVpn7IL56J5xIvnv9K");
        User u3 = new User(3L, "user3", "$2a$10$I7PssiJfoPU58ceXTkDiauclM8hw6yVt6pBSVpn7IL56J5xIvnv9K");
        User u4 = new User(4L, "user4", "$2a$10$I7PssiJfoPU58ceXTkDiauclM8hw6yVt6pBSVpn7IL56J5xIvnv9K");
        List<User> users = new ArrayList<>(List.of(u1, u2, u3, u4));
        users.forEach(user -> userRepository.save(user));

        File f1 = new File(1L, u1, "img001.jpg");
        File f2 = new File(2L, u1, "backup.zip");
        File f3 = new File(3L, u2, "photo001.jpg");
        File f4 = new File(4L, u3, "img001.jpg");
        File f5 = new File(5L, u3, "notes.txt");
        List<File> files = new ArrayList<>(List.of(f1, f2, f3, f4, f5));
        files.forEach(file -> fileRepository.save(file));

        UserStorage us1 = new UserStorage(u1, f1);
        UserStorage us2 = new UserStorage(u1, f2);
        UserStorage us3 = new UserStorage(u2, f3);
        UserStorage us4 = new UserStorage(u3, f4);
        UserStorage us5 = new UserStorage(u3, f5);
        UserStorage us6 = new UserStorage(u4, f1);
        List<UserStorage> usersFiles = new ArrayList<>(List.of(us1, us2, us3, us4, us5, us6));
        usersFiles.forEach(userFile -> userStorageRepository.save(userFile));
    }
}
