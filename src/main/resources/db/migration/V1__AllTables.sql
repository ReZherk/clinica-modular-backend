-- ===================================
-- Flyway Migration V1
-- Description: Create initial database schema for medical appointment system
-- Author: Patrick
-- Date: 2025-09-11
-- ===================================

-- Tablas base sin dependencias
CREATE TABLE Usuarios (
    Id_Usuario INT AUTO_INCREMENT PRIMARY KEY,
    Nombres VARCHAR(100) NOT NULL,
    Apellidos VARCHAR(100) NOT NULL,
    PasswordHash VARCHAR(255) NOT NULL,
    Salt VARCHAR(50),
    Email VARCHAR(150) UNIQUE,
    Telefono VARCHAR(20),
    EstadoRegistro TINYINT(1) NOT NULL DEFAULT 1 -- 1 = activo, 0 = inactivo
);

CREATE TABLE Seguro (
    Id_Seguro INT AUTO_INCREMENT PRIMARY KEY,
    NombreSeguro VARCHAR(100) NOT NULL
);

CREATE TABLE Ubigeo (
    Id_Ubigeo INT PRIMARY KEY,
    Departamento VARCHAR(100),
    Provincia VARCHAR(100),
    Distrito VARCHAR(100)
);

CREATE TABLE Especialidad (
    Id_Especialidad INT AUTO_INCREMENT PRIMARY KEY,
    NombreEspecialidad VARCHAR(100) NOT NULL,
    Descripcion VARCHAR(255)
);

CREATE TABLE Roles_Perfil (
    Id_Perfil INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(50),
    Descripcion VARCHAR(255),
    EstadoRegistro TINYINT(1) NOT NULL DEFAULT 1
);

CREATE TABLE OpcionMenu (
    Id_OpcionMenu INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(100),
    UrlMenu VARCHAR(100),
    Descripcion VARCHAR(255),
    Id_Padre INT NULL,
    EstadoRegistro TINYINT(1) NOT NULL DEFAULT 1,
    FOREIGN KEY (Id_Padre) REFERENCES OpcionMenu (Id_OpcionMenu)
);

-- Tablas con dependencias de primer nivel
CREATE TABLE Tarifario (
    Id_Tarifario INT AUTO_INCREMENT PRIMARY KEY,
    Id_Especialidad INT,
    Monto DECIMAL(10, 2),
    FechaVigencia DATE,
    Activo TINYINT(1) NOT NULL DEFAULT 1, -- 1 = vigente, 0 = no vigente
    FOREIGN KEY (Id_Especialidad) REFERENCES Especialidad (Id_Especialidad)
);

CREATE TABLE Paciente_Detalle (
    Id_Usuario INT PRIMARY KEY,
    DNI CHAR(8) UNIQUE,
    FechaNacimiento DATE,
    Id_Seguro INT,
    Id_Ubigeo INT,
    Direccion VARCHAR(255),
    FOREIGN KEY (Id_Usuario) REFERENCES Usuarios (Id_Usuario),
    FOREIGN KEY (Id_Seguro) REFERENCES Seguro (Id_Seguro),
    FOREIGN KEY (Id_Ubigeo) REFERENCES Ubigeo (Id_Ubigeo)
);

CREATE TABLE Medico_Detalle (
    Id_Usuario INT PRIMARY KEY,
    CMP VARCHAR(20),
    Id_Especialidad INT,
    FOREIGN KEY (Id_Usuario) REFERENCES Usuarios (Id_Usuario),
    FOREIGN KEY (Id_Especialidad) REFERENCES Especialidad (Id_Especialidad)
);

CREATE TABLE Sede (
    Id_Sede INT AUTO_INCREMENT PRIMARY KEY,
    Nombre VARCHAR(100),
    Id_Ubigeo INT,
    Direccion VARCHAR(255),
    Telefono VARCHAR(20),
    FOREIGN KEY (Id_Ubigeo) REFERENCES Ubigeo (Id_Ubigeo)
);

CREATE TABLE Usuario_Perfil (
    Id_Usuario INT,
    Id_Perfil INT,
    PRIMARY KEY (Id_Usuario, Id_Perfil),
    FOREIGN KEY (Id_Usuario) REFERENCES Usuarios (Id_Usuario),
    FOREIGN KEY (Id_Perfil) REFERENCES Roles_Perfil (Id_Perfil)
);

CREATE TABLE Perfil_OpcionMenu (
    Id_Perfil INT,
    Id_OpcionMenu INT,
    PRIMARY KEY (Id_Perfil, Id_OpcionMenu),
    FOREIGN KEY (Id_Perfil) REFERENCES Roles_Perfil (Id_Perfil),
    FOREIGN KEY (Id_OpcionMenu) REFERENCES OpcionMenu (Id_OpcionMenu)
);

-- Tablas con dependencias múltiples
CREATE TABLE Horario_Medico (
    Id_Horario INT AUTO_INCREMENT PRIMARY KEY,
    Id_UsuarioMedico INT,
    DiaSemana VARCHAR(20),
    HoraInicio TIME,
    HoraFin TIME,
    FOREIGN KEY (Id_UsuarioMedico) REFERENCES Medico_Detalle (Id_Usuario)
);

CREATE TABLE Cita (
    Id_Cita INT AUTO_INCREMENT PRIMARY KEY,
    FechaHora DATETIME,
    Id_UsuarioPaciente INT,
    Id_UsuarioMedico INT,
    Id_Especialidad INT,
    Estado TINYINT(1) NOT NULL DEFAULT 1, -- 1 = activa, 0 = cancelada
    Id_Sede INT,
    Id_Tarifa INT,
    FOREIGN KEY (Id_UsuarioPaciente) REFERENCES Paciente_Detalle (Id_Usuario),
    FOREIGN KEY (Id_UsuarioMedico) REFERENCES Medico_Detalle (Id_Usuario),
    FOREIGN KEY (Id_Especialidad) REFERENCES Especialidad (Id_Especialidad),
    FOREIGN KEY (Id_Sede) REFERENCES Sede (Id_Sede),
    FOREIGN KEY (Id_Tarifa) REFERENCES Tarifario (Id_Tarifario)
);

CREATE TABLE Intento_Login (
    Id_Intento INT AUTO_INCREMENT PRIMARY KEY,
    Id_Usuario INT,
    Token VARCHAR(100),
    FechaIntento DATETIME,
    Exitoso TINYINT(1) NOT NULL DEFAULT 0, -- 1 = sí, 0 = no
    IpOrigen VARCHAR(50),
    FOREIGN KEY (Id_Usuario) REFERENCES Usuarios (Id_Usuario)
);

CREATE TABLE Token_Sesion (
    Id_Token INT AUTO_INCREMENT PRIMARY KEY,
    Id_Usuario INT,
    Token VARCHAR(100),
    FechaExpiracion DATETIME,
    Activo TINYINT(1) NOT NULL DEFAULT 1, -- 1 = activo, 0 = inactivo
    FOREIGN KEY (Id_Usuario) REFERENCES Usuarios (Id_Usuario)
);

-- Tablas que dependen de Cita
CREATE TABLE Detalle_Cita (
    Id_Detalle INT AUTO_INCREMENT PRIMARY KEY,
    Id_Cita INT,
    Diagnostico VARCHAR(255),
    Receta VARCHAR(255),
    FOREIGN KEY (Id_Cita) REFERENCES Cita (Id_Cita)
);

CREATE TABLE Documentos_Clinicos (
    Id_Documento INT AUTO_INCREMENT PRIMARY KEY,
    Id_Detalle INT,
    Documento VARCHAR(255),
    FOREIGN KEY (Id_Detalle) REFERENCES Detalle_Cita (Id_Detalle)
);