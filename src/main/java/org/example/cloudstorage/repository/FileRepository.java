package org.example.cloudstorage.repository;

import org.example.cloudstorage.domain.entity.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    @Query("""
            SELECT f
            FROM File f
            JOIN UserStorage us
            ON f = us.file
            JOIN User u
            ON u = us.user
            WHERE u.name = :userName
            AND (:ownerName IS NULL OR f.owner.name = :ownerName)
            AND (:fileType IS NULL OR f.type = :fileType)""")
    Page<File> findFilesByUserName(String userName, String ownerName, String fileType, Pageable pageable);

    @Query("""
            SELECT f
            FROM File f
            WHERE f.name = :fileName
            """)
    File findFileByFileName(String fileName);
}
