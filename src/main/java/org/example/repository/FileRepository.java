package org.example.repository;

import org.example.domain.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    @Query("""
            SELECT f
            FROM UserStorage us
            JOIN us.user u
            JOIN us.file f
            WHERE u.name = :userName""")
    List<File> findFilesByUserName(String userName);
}
