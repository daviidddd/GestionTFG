-- -------------------------------------------------------------
-- TablePlus 5.9.6(546)
--
-- https://tableplus.com/
--
-- Database: gestion
-- Generation Time: 2024-04-12 20:28:00.4830
-- -------------------------------------------------------------


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE gestion;

USE gestion;

CREATE TABLE `alumnos` (
                           `id_ucam` int NOT NULL,
                           `nombre` int NOT NULL,
                           `apellido1` varchar(5) NOT NULL,
                           `apellido2` varchar(12) NOT NULL,
                           `correo` varchar(50) NOT NULL,
                           `nia` int NOT NULL,
                           `expediente` varchar(2) DEFAULT NULL,
                           PRIMARY KEY (`id_ucam`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `asignatura` (
                              `id` int NOT NULL AUTO_INCREMENT,
                              `nombre` varchar(100) NOT NULL,
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `autorizados` (
                               `correo` varchar(25) NOT NULL,
                               `contrasena` varchar(25) NOT NULL,
                               `ult_inicio_sesion` datetime DEFAULT NULL,
                               PRIMARY KEY (`correo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `puntuaciones` (
                                `tfg` varchar(20) NOT NULL,
                                `orden` int NOT NULL,
                                `alumno` int NOT NULL,
                                `puntuacion` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `solicitudes` (
                               `correo` varchar(55) NOT NULL,
                               `nia` int NOT NULL,
                               `nota_media` double NOT NULL,
                               `creditos_restantes` double NOT NULL,
                               `meses_experiencia` double NOT NULL,
                               `meritos` varchar(2) NOT NULL,
                               `tfg1` varchar(15) DEFAULT NULL,
                               `tfg2` varchar(15) DEFAULT NULL,
                               `tfg3` varchar(15) DEFAULT NULL,
                               `tfg4` varchar(15) DEFAULT NULL,
                               `tfg5` varchar(15) DEFAULT NULL,
                               `exp_tfg1` double NOT NULL,
                               `exp_tfg2` double NOT NULL,
                               `exp_tfg3` double NOT NULL,
                               `exp_tfg4` double NOT NULL,
                               `exp_tfg5` double NOT NULL,
                               `pto_creditos` int NOT NULL,
                               `pto_experiencia` int NOT NULL,
                               `pto_nota_media` int NOT NULL,
                               `pto_tfg1` int NOT NULL,
                               `pto_tfg2` int NOT NULL,
                               `pto_tfg3` int NOT NULL,
                               `pto_tfg4` int NOT NULL,
                               `pto_tfg5` int NOT NULL,
                               PRIMARY KEY (`correo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `tfgs` (
                        `id` int NOT NULL AUTO_INCREMENT,
                        `codigo` varchar(1000) DEFAULT NULL,
                        `titulo` text,
                        `descripcion` text,
                        `tutor` text,
                        `asignaturas` text,
                        `solicitantes` int DEFAULT NULL,
                        `adjudicado` varchar(50) DEFAULT NULL,
                        `tecnologias` text,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1123 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `tutor` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `nombre` varchar(100) NOT NULL,
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
