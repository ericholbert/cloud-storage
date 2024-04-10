package org.example.cloudstorage.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(UserStorage.Pk.class)
@Table(name = "users_files")
public class UserStorage {
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class Pk implements Serializable {
        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;
        @ManyToOne
        @JoinColumn(name = "file_id")
        private File file;
    }

    @Id
    private User user;
    @Id
    private File file;
}
