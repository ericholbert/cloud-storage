package org.example.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.Arrays;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "files")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    private String name;
    private String type;
    private Long size;
    private String path;

    public File(User owner, String name, String type, Long size, String path) {
        this.owner = owner;
        this.name = name;
        this.type = type;
        this.size = size;
        this.path = path;
    }

    // Maybe next time I should use something like DB view instead of bothering with checks for valid sort fields
    public static boolean hasValidSortField(Sort sort) {
        return sort.stream()
                .allMatch(order -> Arrays.stream(File.class.getDeclaredFields())
                        .filter(field -> !field.getName().equals("path"))
                        .anyMatch(field -> order.getProperty().equals(field.getName()))
        );
    }
}
