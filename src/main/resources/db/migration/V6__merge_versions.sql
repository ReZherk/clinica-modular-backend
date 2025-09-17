-- ===================================
-- Script consolidado de la BD clinica_db
-- Incluye cambios hasta V5 (usuarios, paciente_detalle, tipo_documento, tarifa en especialidad)
-- ===================================

-- Crear base de datos si no existe
CREATE DATABASE IF NOT EXISTS clinica_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

USE clinica_db;

-- =========================
-- Tabla: tipo_documento
-- =========================
CREATE TABLE IF NOT EXISTS tipo_documento (
    Id_TipoDocumento INT NOT NULL AUTO_INCREMENT,
    Nombre VARCHAR(50) NOT NULL,
    Descripcion VARCHAR(255),
    EstadoRegistro TINYINT(1) NOT NULL DEFAULT 1,
    PRIMARY KEY (Id_TipoDocumento)
);

-- =========================
-- Tabla: usuarios
-- =========================
CREATE TABLE IF NOT EXISTS usuarios (
    Id_Usuario INT NOT NULL AUTO_INCREMENT,
    Id_TipoDocumento INT,
    Nombres VARCHAR(100) NOT NULL,
    Apellidos VARCHAR(100) NOT NULL,
    DNI CHAR(12) UNIQUE,
    Fecha_Emision DATE,
    PasswordHash VARCHAR(255) NOT NULL,
    Salt VARCHAR(50) DEFAULT NULL,
    Email VARCHAR(150) UNIQUE,
    Telefono VARCHAR(20),
    EstadoRegistro TINYINT(1) NOT NULL DEFAULT 1,
    Fecha_Creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (Id_Usuario),
    CONSTRAINT fk_usuarios_tipodoc FOREIGN KEY (Id_TipoDocumento) REFERENCES tipo_documento (Id_TipoDocumento)
);

-- =========================
-- Tabla: roles_perfil
-- =========================
CREATE TABLE IF NOT EXISTS roles_perfil (
    Id_Perfil INT NOT NULL AUTO_INCREMENT,
    Nombre VARCHAR(50),
    Descripcion VARCHAR(255),
    EstadoRegistro TINYINT(1) NOT NULL DEFAULT 1,
    PRIMARY KEY (Id_Perfil)
);

-- =========================
-- Tabla: usuario_perfil
-- =========================
CREATE TABLE IF NOT EXISTS usuario_perfil (
    Id_Usuario INT NOT NULL,
    Id_Perfil INT NOT NULL,
    PRIMARY KEY (Id_Usuario, Id_Perfil),
    FOREIGN KEY (Id_Usuario) REFERENCES usuarios (Id_Usuario),
    FOREIGN KEY (Id_Perfil) REFERENCES roles_perfil (Id_Perfil)
);

-- =========================
-- Tabla: especialidad
-- =========================
CREATE TABLE IF NOT EXISTS especialidad (
    Id_Especialidad INT NOT NULL AUTO_INCREMENT,
    NombreEspecialidad VARCHAR(100) NOT NULL,
    Descripcion VARCHAR(255),
    Tarifa DECIMAL(10, 2) NULL,
    PRIMARY KEY (Id_Especialidad)
);

-- =========================
-- Tabla: paciente_detalle
-- =========================
CREATE TABLE IF NOT EXISTS paciente_detalle (
    Id_Usuario INT NOT NULL,
    FechaNacimiento DATE,
    Direccion VARCHAR(255),
    Departamento VARCHAR(100),
    Provincia VARCHAR(100),
    Distrito VARCHAR(100),
    PRIMARY KEY (Id_Usuario),
    FOREIGN KEY (Id_Usuario) REFERENCES usuarios (Id_Usuario)
);

-- =========================
-- Tabla: medico_detalle
-- =========================
CREATE TABLE IF NOT EXISTS medico_detalle (
    Id_Usuario INT NOT NULL,
    CMP VARCHAR(20),
    Id_Especialidad INT,
    PRIMARY KEY (Id_Usuario),
    FOREIGN KEY (Id_Usuario) REFERENCES usuarios (Id_Usuario),
    FOREIGN KEY (Id_Especialidad) REFERENCES especialidad (Id_Especialidad)
);

-- =========================
-- Tabla: intento_login
-- =========================
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

-- =========================
-- Tabla: token_sesion
-- =========================
CREATE TABLE IF NOT EXISTS token_sesion (
    Id_Token INT NOT NULL AUTO_INCREMENT,
    Id_Usuario INT,
    Token VARCHAR(100),
    FechaExpiracion DATETIME,
    Activo TINYINT(1) NOT NULL DEFAULT 1,
    PRIMARY KEY (Id_Token),
    FOREIGN KEY (Id_Usuario) REFERENCES usuarios (Id_Usuario)
);

-- =========================
-- Tabla: opcionmenu
-- =========================
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