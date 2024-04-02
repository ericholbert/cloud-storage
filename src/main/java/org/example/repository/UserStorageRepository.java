package org.example.repository;

import org.example.domain.entity.UserStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStorageRepository extends JpaRepository<UserStorage, UserStorage.Pk> {
}
