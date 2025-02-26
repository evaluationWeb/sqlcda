-- Création de la base de données
CREATE DATABASE IF NOT EXISTS sqlcda CHARSET utf8mb4;
USE sqlcda;

-- Ajout des tables
CREATE TABLE IF NOT EXISTS users(
id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
firstname VARCHAR(50) NOT NULL,
lastname VARCHAR(50) NOT NULL,
email VARCHAR(50) UNIQUE NOT NULL,
`password` VARCHAR(100) NOT NULL 
);

CREATE TABLE IF NOT EXISTS roles(
id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
roles_name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS task(
id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
title VARCHAR(50) NOT NULL,
content VARCHAR(255) NOT NULL,
create_at DATETIME DEFAULT NOW(),
end_date DATETIME, 
`status` TINYINT(1) DEFAULT 0,
users_id INT
);

CREATE TABLE IF NOT EXISTS category(
id INT PRIMARY KEY AUTO_INCREMENT NOT NULL,
category_name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS task_category(
task_id INT,
category_id INT,
PRIMARY KEY(task_id, category_id)
);

-- Edition des tables et contraintes
ALTER TABLE users
ADD COLUMN roles_id INT;

ALTER TABLE users
ADD CONSTRAINT fk_assign_roles
FOREIGN KEY(roles_id)
REFERENCES roles(id);

ALTER TABLE task
ADD CONSTRAINT fk_write_users
FOREIGN KEY(users_id)
REFERENCES users(id);

ALTER TABLE task_category
ADD CONSTRAINT fk_link_task
FOREIGN KEY(task_id)
REFERENCES task(id);

ALTER TABLE task_category
ADD CONSTRAINT fk_link_category
FOREIGN KEY(task_id)
REFERENCES task(id);

-- Ajout de données
INSERT INTO roles(roles_name) VALUES
("USER"),("CLIENT"),("ADMIN");

INSERT INTO category(category_name) VALUES
("Travail"),("Perso"),("Cinema"),
("Sport"),("Musique"),("Cuisine");