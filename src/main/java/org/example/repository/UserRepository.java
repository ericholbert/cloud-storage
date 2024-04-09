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
            FROM User u
            JOIN UserStorage us
            ON u = us.user
            JOIN File f
            ON f = us.file
            WHERE f.id = :fileId
            ORDER BY u.name""")
    List<User> findShareUsersByFileId(Long fileId);

    @Query("""
            SELECT u
            FROM User u
            WHERE u.name = :userName""")
    User findUserByUserName(String userName);
}
