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

-- The passwords are 'password'
INSERT INTO users VALUES
	(1, 'user1', '$2a$10$I7PssiJfoPU58ceXTkDiauclM8hw6yVt6pBSVpn7IL56J5xIvnv9K'),
	(2, 'user2', '$2a$10$I7PssiJfoPU58ceXTkDiauclM8hw6yVt6pBSVpn7IL56J5xIvnv9K'),
    (3, 'user3', '$2a$10$I7PssiJfoPU58ceXTkDiauclM8hw6yVt6pBSVpn7IL56J5xIvnv9K'),
    (4, 'user4', '$2a$10$I7PssiJfoPU58ceXTkDiauclM8hw6yVt6pBSVpn7IL56J5xIvnv9K');

INSERT INTO files VALUES
	(1, 1, 'img001.jpg'),
    (2, 1, 'backup.zip'),
    (3, 2, 'photo001.jpg'),
    (4, 3, 'img001.jpg'),
    (5, 3, 'notes.txt');

INSERT INTO users_files VALUES
	(1, 1),
    (1, 2),
    (2, 3),
    (3, 4),
    (3, 5),
    (4, 1);
