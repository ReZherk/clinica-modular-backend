-- =============================================
-- Flyway Migration V16
-- Descripción: Creación completa del esquema actualizado del sistema
-- Autor: Sistema
-- Fecha: 2025-10-22
-- =============================================

-- =============================================
-- Tabla: tipo_documento
-- =============================================
CREATE TABLE IF NOT EXISTS `tipo_documento` (
    `Id_TipoDocumento` INT NOT NULL AUTO_INCREMENT,
    `Nombre` VARCHAR(50) NOT NULL,
    `Descripcion` VARCHAR(255) DEFAULT NULL,
    `EstadoRegistro` TINYINT(1) NOT NULL DEFAULT '1',
    PRIMARY KEY (`Id_TipoDocumento`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- =============================================
-- Tabla: especialidad
-- =============================================
CREATE TABLE IF NOT EXISTS `especialidad` (
    `Id_Especialidad` INT NOT NULL AUTO_INCREMENT,
    `NombreEspecialidad` VARCHAR(100) NOT NULL,
    `Descripcion` VARCHAR(255) DEFAULT NULL,
    `Tarifa` DECIMAL(10, 2) DEFAULT NULL,
    `EstadoRegistro` TINYINT(1) NOT NULL DEFAULT '1',
    PRIMARY KEY (`Id_Especialidad`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- =============================================
-- Tabla: usuarios
-- =============================================
CREATE TABLE IF NOT EXISTS `usuarios` (
    `Id_Usuario` INT NOT NULL AUTO_INCREMENT,
    `Id_TipoDocumento` INT DEFAULT NULL,
    `Nombres` VARCHAR(100) NOT NULL,
    `Apellidos` VARCHAR(100) NOT NULL,
    `NumeroDocumento` CHAR(12) DEFAULT NULL,
    `PasswordHash` VARCHAR(255) NOT NULL,
    `Email` VARCHAR(150) DEFAULT NULL,
    `Telefono` VARCHAR(20) DEFAULT NULL,
    `EstadoRegistro` TINYINT(1) NOT NULL DEFAULT '1',
    `Fecha_Creacion` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`Id_Usuario`),
    UNIQUE KEY `Email` (`Email`),
    UNIQUE KEY `NumeroDocumento` (`NumeroDocumento`),
    KEY `fk_usuarios_tipodoc` (`Id_TipoDocumento`),
    CONSTRAINT `fk_usuarios_tipodoc` FOREIGN KEY (`Id_TipoDocumento`) REFERENCES `tipo_documento` (`Id_TipoDocumento`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- =============================================
-- Tabla: roles_perfil
-- =============================================
CREATE TABLE IF NOT EXISTS `roles_perfil` (
    `Id_Perfil` INT NOT NULL AUTO_INCREMENT,
    `Nombre` VARCHAR(50) DEFAULT NULL,
    `Descripcion` VARCHAR(255) DEFAULT NULL,
    `EstadoRegistro` TINYINT(1) NOT NULL DEFAULT '1',
    PRIMARY KEY (`Id_Perfil`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- =============================================
-- Tabla: permissions
-- =============================================
CREATE TABLE IF NOT EXISTS `permissions` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `description` VARCHAR(500) DEFAULT NULL,
    `action_key` VARCHAR(100) NOT NULL,
    `estado_registro` TINYINT(1) NOT NULL DEFAULT '1',
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`),
    UNIQUE KEY `action_key` (`action_key`),
    KEY `idx_action_key` (`action_key`),
    KEY `idx_name` (`name`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- =============================================
-- Tabla: medico_detalle
-- =============================================
CREATE TABLE IF NOT EXISTS `medico_detalle` (
    `Id_Usuario` INT NOT NULL,
    `CMP` VARCHAR(20) DEFAULT NULL,
    `Id_Especialidad` INT DEFAULT NULL,
    PRIMARY KEY (`Id_Usuario`),
    KEY `Id_Especialidad` (`Id_Especialidad`),
    CONSTRAINT `medico_detalle_ibfk_1` FOREIGN KEY (`Id_Usuario`) REFERENCES `usuarios` (`Id_Usuario`),
    CONSTRAINT `medico_detalle_ibfk_2` FOREIGN KEY (`Id_Especialidad`) REFERENCES `especialidad` (`Id_Especialidad`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- =============================================
-- Tabla: paciente_detalle
-- =============================================
CREATE TABLE IF NOT EXISTS `paciente_detalle` (
    `Id_Usuario` INT NOT NULL,
    `FechaNacimiento` DATE DEFAULT NULL,
    `Direccion` VARCHAR(255) DEFAULT NULL,
    `Departamento` VARCHAR(100) DEFAULT NULL,
    `Provincia` VARCHAR(100) DEFAULT NULL,
    `Distrito` VARCHAR(100) DEFAULT NULL,
    PRIMARY KEY (`Id_Usuario`),
    CONSTRAINT `paciente_detalle_ibfk_1` FOREIGN KEY (`Id_Usuario`) REFERENCES `usuarios` (`Id_Usuario`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- =============================================
-- Tabla: usuario_perfil
-- =============================================
CREATE TABLE IF NOT EXISTS `usuario_perfil` (
    `Id_Usuario` INT NOT NULL,
    `Id_Perfil` INT NOT NULL,
    PRIMARY KEY (`Id_Usuario`, `Id_Perfil`),
    KEY `Id_Perfil` (`Id_Perfil`),
    CONSTRAINT `usuario_perfil_ibfk_1` FOREIGN KEY (`Id_Usuario`) REFERENCES `usuarios` (`Id_Usuario`),
    CONSTRAINT `usuario_perfil_ibfk_2` FOREIGN KEY (`Id_Perfil`) REFERENCES `roles_perfil` (`Id_Perfil`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- =============================================
-- Tabla: roles_perfil_permisos
-- =============================================
CREATE TABLE IF NOT EXISTS `roles_perfil_permisos` (
    `Id_Perfil` INT NOT NULL,
    `permission_id` BIGINT NOT NULL,
    PRIMARY KEY (`Id_Perfil`, `permission_id`),
    KEY `fk_roles_perfil_permisos_permiso` (`permission_id`),
    CONSTRAINT `fk_roles_perfil_permisos_permiso` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_roles_perfil_permisos_rol` FOREIGN KEY (`Id_Perfil`) REFERENCES `roles_perfil` (`Id_Perfil`) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- =============================================
-- Tabla: token_sesion
-- =============================================
CREATE TABLE IF NOT EXISTS `token_sesion` (
    `Id_Token` INT NOT NULL AUTO_INCREMENT,
    `Id_Usuario` INT DEFAULT NULL,
    `Token` VARCHAR(100) DEFAULT NULL,
    `FechaExpiracion` DATETIME DEFAULT NULL,
    `Activo` TINYINT(1) NOT NULL DEFAULT '1',
    PRIMARY KEY (`Id_Token`),
    KEY `Id_Usuario` (`Id_Usuario`),
    CONSTRAINT `token_sesion_ibfk_1` FOREIGN KEY (`Id_Usuario`) REFERENCES `usuarios` (`Id_Usuario`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- =============================================
-- Tabla: intento_login
-- =============================================
CREATE TABLE IF NOT EXISTS `intento_login` (
    `Id_Intento` INT NOT NULL AUTO_INCREMENT,
    `Id_Usuario` INT DEFAULT NULL,
    `Token` VARCHAR(100) DEFAULT NULL,
    `FechaIntento` DATETIME DEFAULT NULL,
    `Exitoso` TINYINT(1) NOT NULL DEFAULT '0',
    `IpOrigen` VARCHAR(50) DEFAULT NULL,
    PRIMARY KEY (`Id_Intento`),
    KEY `Id_Usuario` (`Id_Usuario`),
    CONSTRAINT `intento_login_ibfk_1` FOREIGN KEY (`Id_Usuario`) REFERENCES `usuarios` (`Id_Usuario`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- =============================================
-- Fin de migración V16
-- =============================================