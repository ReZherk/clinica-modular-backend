-- ===================================
-- V3__verify_and_create_tables.sql
-- Descripción: Verifica que existan las nuevas tablas iniciales.
-- ===================================

-- Crear la base si no existe
CREATE DATABASE IF NOT EXISTS clinica_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

USE clinica_db;

-- Usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    Id_Usuario INT NOT NULL AUTO_INCREMENT,
    Nombres VARCHAR(100) NOT NULL,
    Apellidos VARCHAR(100) NOT NULL,
    PasswordHash VARCHAR(255) NOT NULL,
    Salt VARCHAR(50) DEFAULT NULL,
    Email VARCHAR(150) UNIQUE,
    Telefono VARCHAR(20),
    EstadoRegistro TINYINT(1) NOT NULL DEFAULT 1,
    PRIMARY KEY (Id_Usuario)
);

-- Roles_Perfil
CREATE TABLE IF NOT EXISTS roles_perfil (
    Id_Perfil INT NOT NULL AUTO_INCREMENT,
    Nombre VARCHAR(50),
    Descripcion VARCHAR(255),
    EstadoRegistro TINYINT(1) NOT NULL DEFAULT 1,
    PRIMARY KEY (Id_Perfil)
);

-- Usuario_Perfil
CREATE TABLE IF NOT EXISTS usuario_perfil (
    Id_Usuario INT NOT NULL,
    Id_Perfil INT NOT NULL,
    PRIMARY KEY (Id_Usuario, Id_Perfil),
    FOREIGN KEY (Id_Usuario) REFERENCES usuarios (Id_Usuario),
    FOREIGN KEY (Id_Perfil) REFERENCES roles_perfil (Id_Perfil)
);

-- Especialidad
CREATE TABLE IF NOT EXISTS especialidad (
    Id_Especialidad INT NOT NULL AUTO_INCREMENT,
    NombreEspecialidad VARCHAR(100) NOT NULL,
    Descripcion VARCHAR(255),
    PRIMARY KEY (Id_Especialidad)
);

-- Paciente_Detalle
CREATE TABLE IF NOT EXISTS paciente_detalle (
    Id_Usuario INT NOT NULL,
    DNI CHAR(8) UNIQUE,
    FechaNacimiento DATE,
    Direccion VARCHAR(255),
    PRIMARY KEY (Id_Usuario),
    FOREIGN KEY (Id_Usuario) REFERENCES usuarios (Id_Usuario)
);

-- Medico_Detalle
CREATE TABLE IF NOT EXISTS medico_detalle (
    Id_Usuario INT NOT NULL,
    CMP VARCHAR(20),
    Id_Especialidad INT,
    PRIMARY KEY (Id_Usuario),
    FOREIGN KEY (Id_Usuario) REFERENCES usuarios (Id_Usuario),
    FOREIGN KEY (Id_Especialidad) REFERENCES especialidad (Id_Especialidad)
);

-- Intento_Login
CREATE TABLE IF NOT EXISTS intento_login (
    Id_Intento INT NOT NULL AUTO_INCREMENT,
    Id_Usuario INT,
    Token VARCHAR(100),
    FechaIntento DATETIME,
    Exitoso TINYINT(1) NOT NULL DEFAULT 0,
    IpOrigen VARCHAR(50),
    PRIMARY KEY (Id_Intento),
    FOREIGN KEY (Id_Usuario) REFERENCES usuarios (Id_Usuario)
);

-- Token_Sesion
CREATE TABLE IF NOT EXISTS token_sesion (
    Id_Token INT NOT NULL AUTO_INCREMENT,
    Id_Usuario INT,
    Token VARCHAR(100),
    FechaExpiracion DATETIME,
    Activo TINYINT(1) NOT NULL DEFAULT 1,
    PRIMARY KEY (Id_Token),
    FOREIGN KEY (Id_Usuario) REFERENCES usuarios (Id_Usuario)
);

-- OpcionMenu
CREATE TABLE IF NOT EXISTS opcionmenu (
    Id_OpcionMenu INT NOT NULL AUTO_INCREMENT,
    Nombre VARCHAR(100),
    UrlMenu VARCHAR(100),
    Descripcion VARCHAR(255),
    Id_Padre INT,
    EstadoRegistro TINYINT(1) NOT NULL DEFAULT 1,
    PRIMARY KEY (Id_OpcionMenu),
    FOREIGN KEY (Id_Padre) REFERENCES opcionmenu (Id_OpcionMenu)
);