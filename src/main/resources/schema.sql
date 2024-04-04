CREATE DATABASE IF NOT EXISTS user_storage_db;

USE cloud_storage_db;

DROP TABLE IF EXISTS users, files, users_files;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT,
    name VARCHAR(45) NOT NULL UNIQUE,
    password CHAR(72) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE files (
    id BIGINT AUTO_INCREMENT,
    owner_id BIGINT NOT NULL,
    name VARCHAR(45) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_owner
        FOREIGN KEY (owner_id)
        REFERENCES users(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE users_files (
    user_id BIGINT,
    file_id BIGINT,
    PRIMARY KEY (user_id, file_id),
    CONSTRAINT fk_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT fk_file
        FOREIGN KEY (file_id)
        REFERENCES files(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);
