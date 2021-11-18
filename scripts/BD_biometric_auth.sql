CREATE DATABASE biometric_auth;

CREATE TABLE addresses (
    id INT(3) AUTO_INCREMENT,
    street VARCHAR(50) NOT NULL,
    complement VARCHAR(50) NOT NULL,
    state VARCHAR(2) NOT NULL,
    neighborhood VARCHAR(30) NOT NULL,
    city VARCHAR(30) NOT NULL,
    number INT(5) NOT NULL,
    postal_code INT(8) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE pesticides (
    id INT(3) AUTO_INCREMENT,
    description VARCHAR(30) NOT NULL,
    risk VARCHAR(30) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE levels (
    id INT(3) AUTO_INCREMENT,
    description VARCHAR(30) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE roles (
    id INT(3) AUTO_INCREMENT,
    description VARCHAR(30) NOT NULL,
    level_ID INT(1) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE roles
ADD CONSTRAINT fk_level_ID_roles
FOREIGN KEY (level_ID) REFERENCES levels(id);

CREATE TABLE infos (
    id INT(3) AUTO_INCREMENT,
    level_ID INT(3) NOT NULL,
    description VARCHAR(30) NOT NULL,
    pesticide_ID INT(3) NOT NULL,
    address_ID INT(3) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE infos
ADD CONSTRAINT fk_level_ID_infos
FOREIGN KEY (level_ID) REFERENCES levels(id);

ALTER TABLE infos
ADD CONSTRAINT fk_pesticide_ID_infos
FOREIGN KEY (pesticide_ID) REFERENCES pesticides(id);

ALTER TABLE infos
ADD CONSTRAINT fk_addresses_ID_infos
FOREIGN KEY (address_ID) REFERENCES addresses(id);

CREATE TABLE users (
    id INT(3)AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL,
    last_name VARCHAR(40) NOT NULL,
    role_ID INT(3) NOT NULL,
    username VARCHAR(10) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE users
ADD CONSTRAINT fk_role_ID_users
FOREIGN KEY (role_ID) REFERENCES roles(id);


CREATE TABLE digitalImpressions (
    id INT(3) AUTO_INCREMENT,
    image LONGBLOB NOT NULL,
    user_ID INT(3) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE digitalImpressions
ADD CONSTRAINT fk_user_ID_digitalImpressions
FOREIGN KEY (user_ID) REFERENCES users(id);
