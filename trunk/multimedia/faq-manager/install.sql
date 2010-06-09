DROP DATABASE IF EXISTS mhrv;

CREATE DATABASE mhrv CHARACTER SET utf8 COLLATE utf8_bin;
USE mhrv;

CREATE TABLE H10_12_categoria_faq (
    id INT NOT NULL AUTO_INCREMENT,
    categoria VARCHAR(255) UNIQUE NOT NULL default '', 
    PRIMARY KEY (id)
) TYPE=InnoDB AUTO_INCREMENT=1;

CREATE TABLE H10_12_faq (
    id INT NOT NULL AUTO_INCREMENT,
    pregunta VARCHAR(255) UNIQUE NOT NULL default '', 
    respuesta VARCHAR(255) NOT NULL default '',
    idCategoria INT,
    PRIMARY KEY (id),
    FOREIGN KEY (idCategoria) REFERENCES H10_12_categoria_faq (id) ON DELETE SET NULL ON UPDATE CASCADE
) TYPE=InnoDB AUTO_INCREMENT=1;
