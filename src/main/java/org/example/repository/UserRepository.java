package org.example.repository;

import org.example.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("""
            SELECT u
            FROM UserStorage us
            JOIN us.user u
            JOIN us.file f
            WHERE f.id = :fileId""")
    List<User> findShareUsersByFileId(Long fileId);
}
