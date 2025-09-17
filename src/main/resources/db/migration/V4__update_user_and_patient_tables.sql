-- ===================================
-- V4__update_user_and_patient_tables.sql
-- Descripción: Ajustes en usuarios y paciente_detalle + tipo de documento
-- ===================================

USE clinica_db;

-- 1) Cambios en paciente_detalle
ALTER TABLE paciente_detalle
DROP COLUMN DNI,
ADD COLUMN Departamento VARCHAR(100) AFTER Direccion,
ADD COLUMN Provincia VARCHAR(100) AFTER Departamento,
ADD COLUMN Distrito VARCHAR(100) AFTER Provincia;

-- 2) Cambios en usuarios
ALTER TABLE usuarios
ADD COLUMN DNI CHAR(12) UNIQUE AFTER Apellidos,
ADD COLUMN Fecha_Emision DATE AFTER DNI,
ADD COLUMN Fecha_Creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP AFTER EstadoRegistro;

-- 3) Crear tabla tipo_documento
CREATE TABLE IF NOT EXISTS tipo_documento (
    Id_TipoDocumento INT NOT NULL AUTO_INCREMENT,
    Nombre VARCHAR(50) NOT NULL, -- Ej: DNI, Carné de extranjería, Pasaporte
    Descripcion VARCHAR(255),
    EstadoRegistro TINYINT(1) NOT NULL DEFAULT 1,
    PRIMARY KEY (Id_TipoDocumento)
);

-- 3.1) Agregar relación en usuarios
ALTER TABLE usuarios
ADD COLUMN Id_TipoDocumento INT AFTER Id_Usuario,
ADD CONSTRAINT fk_usuarios_tipodoc FOREIGN KEY (Id_TipoDocumento) REFERENCES tipo_documento (Id_TipoDocumento);

-- 3.2) Poblar opciones por defecto
INSERT INTO
    tipo_documento (Nombre, Descripcion)
VALUES (
        'DNI',
        'Documento Nacional de Identidad'
    ),
    (
        'Carné de extranjería',
        'Documento para extranjeros residentes'
    ),
    (
        'Pasaporte',
        'Documento de viaje emitido por un país'
    );