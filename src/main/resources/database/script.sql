CREATE DATABASE gestion CREATE DATABASE gestion CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

DROP USER IF EXISTS 'administrador'@'localhost';
CREATE USER 'administrador'@'localhost' IDENTIFIED BY 'AdministradorTFG_0001';
GRANT ALL PRIVILEGES ON gestion.* TO 'administrador'@'localhost';
FLUSH PRIVILEGES;

USE gestion;

CREATE TABLE alumnos (
                         id_ucam int NOT NULL,
                         nombre int NOT NULL,
                         apellido1 varchar(5) NOT NULL,
                         apellido2 varchar(12) NOT NULL,
                         correo varchar(50) NOT NULL,
                         nia int NOT NULL,
                         expediente varchar(2) DEFAULT NULL,
                         PRIMARY KEY (id_ucam)
);

CREATE TABLE asignatura (
                            id int NOT NULL AUTO_INCREMENT,
                            nombre varchar(100) NOT NULL,
                            PRIMARY KEY (id)
);

CREATE TABLE autorizados (
                             correo varchar(25) NOT NULL,
                             contrasena varchar(25) NOT NULL,
                             ult_inicio_sesion datetime DEFAULT NULL,
                             PRIMARY KEY (correo)
);

CREATE TABLE puntuaciones (
                              tfg varchar(20) NOT NULL,
                              orden int NOT NULL,
                              alumno int NOT NULL,
                              puntuacion int NOT NULL
);

CREATE TABLE solicitudes (
                             correo varchar(55) NOT NULL,
                             nia int NOT NULL,
                             nota_media double NOT NULL,
                             creditos_restantes double NOT NULL,
                             meses_experiencia double NOT NULL,
                             meritos varchar(2) NOT NULL,
                             tfg1 varchar(15) DEFAULT NULL,
                             tfg2 varchar(15) DEFAULT NULL,
                             tfg3 varchar(15) DEFAULT NULL,
                             tfg4 varchar(15) DEFAULT NULL,
                             tfg5 varchar(15) DEFAULT NULL,
                             exp_tfg1 double NOT NULL,
                             exp_tfg2 double NOT NULL,
                             exp_tfg3 double NOT NULL,
                             exp_tfg4 double NOT NULL,
                             exp_tfg5 double NOT NULL,
                             pto_creditos int NOT NULL,
                             pto_experiencia int NOT NULL,
                             pto_nota_media int NOT NULL,
                             pto_tfg1 int NOT NULL,
                             pto_tfg2 int NOT NULL,
                             pto_tfg3 int NOT NULL,
                             pto_tfg4 int NOT NULL,
                             pto_tfg5 int NOT NULL,
                             PRIMARY KEY (correo)
);

CREATE TABLE tfgs (
                      id int NOT NULL AUTO_INCREMENT,
                      codigo varchar(1000) DEFAULT NULL,
                      titulo text,
                      descripcion text,
                      tutor text,
                      asignaturas text,
                      solicitantes int DEFAULT NULL,
                      adjudicado varchar(50) DEFAULT NULL,
                      tecnologias text,
                      PRIMARY KEY (id)
);

CREATE TABLE tutor (
                       id int NOT NULL AUTO_INCREMENT,
                       nombre varchar(100) NOT NULL,
                       PRIMARY KEY (id)
);
