package org.example.cloudstorage.repository;

import org.example.cloudstorage.domain.entity.UserStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserStorageRepository extends JpaRepository<UserStorage, UserStorage.Pk> {
}
