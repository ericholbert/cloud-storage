DROP TABLE IF EXISTS users, files, users_files;

CREATE TABLE users (
id BIGINT AUTO_INCREMENT,
name VARCHAR(45) NOT NULL UNIQUE,
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

INSERT INTO users VALUES
	(1, 'user1'),
	(2, 'user2'),
    (3, 'user3'),
    (4, 'user4');

INSERT INTO files VALUES
	(10, 1, 'img001.jpg'),
    (20, 1, 'backup.zip'),
    (30, 2, 'photo001.jpg'),
    (40, 3, 'img001.jpg'),
    (50, 3, 'notes.txt');

INSERT INTO users_files VALUES
	(1, 10),
    (1, 20),
    (2, 30),
    (3, 40),
    (3, 50),
    (4, 10);