CREATE DATABASE gestion;
USE gestion;

CREATE TABLE autorizados(
    correo VARCHAR(25) NOT NULL,
    contrasena VARCHAR(25) NOT NULL,
    ult_inicio_sesion DATETIME,
    PRIMARY KEY (correo)
);

CREATE TABLE alumnos (
    id_ucam INT NOT NULL,
    nombre INT NOT NULL,
    apellido1 VARCHAR(5) NOT NULL,
    apellido2 VARCHAR(12) NOT NULL,
    correo VARCHAR(50) NOT NULL,
    nia INT NOT NULL,
    PRIMARY KEY (id_ucam)
);

CREATE TABLE tfgs (
    id INT NOT NULL AUTO_INCREMENT,
    codigo VARCHAR(1000),
    titulo TEXT,
    descripcion TEXT,
    tutor TEXT,
    asignaturas TEXT,
    solicitantes INT,
    adjudicado VARCHAR(50),
    tecnologias TEXT,
    PRIMARY KEY (id)
);

CREATE TABLE solicitudes (
    correo VARCHAR(55) NOT NULL,
    nota_media DOUBLE NOT NULL,
    creditos_restantes DOUBLE NOT NULL,
    meses_experiencia DOUBLE NOT NULL,
    meritos VARCHAR(2) NOT NULL,
    tfg1 VARCHAR(15),
    tfg2 VARCHAR(15),
    tfg3 VARCHAR(15),
    tfg4 VARCHAR(15),
    tfg5 VARCHAR(15),
    exp_tfg1 DOUBLE NOT NULL,
    exp_tfg2 DOUBLE NOT NULL,
    exp_tfg3 DOUBLE NOT NULL,
    exp_tfg4 DOUBLE NOT NULL,
    exp_tfg5 DOUBLE NOT NULL,
    PRIMARY KEY (correo)
);

CREATE TABLE asignatura (
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE tutor (
    id INT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
);