CREATE DATABASE IF NOT EXISTS user_storage_db;

USE cloud_storage_db;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL UNIQUE,
    password CHAR(72) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS files (
    id BIGINT AUTO_INCREMENT,
    owner_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    size BIGINT NOT NULL,
    path VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_owner
        FOREIGN KEY (owner_id)
        REFERENCES users(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE IF NOT EXISTS users_files (
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
