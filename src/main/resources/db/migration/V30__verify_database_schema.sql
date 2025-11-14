-- =============================================
-- Migración Flyway V30: Verificación de esquema
-- Propósito: Verificar que todas las tablas existan
-- NO modifica datos ni estructura si ya existe
-- =============================================

-- Verificar tabla tipo_documento
CREATE TABLE IF NOT EXISTS `tipo_documento` (
    `Id_TipoDocumento` int NOT NULL AUTO_INCREMENT,
    `Nombre` varchar(50) NOT NULL,
    `Descripcion` varchar(255) DEFAULT NULL,
    `EstadoRegistro` tinyint(1) NOT NULL DEFAULT '1',
    PRIMARY KEY (`Id_TipoDocumento`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Verificar tabla usuarios
CREATE TABLE IF NOT EXISTS `usuarios` (
    `Id_Usuario` int NOT NULL AUTO_INCREMENT,
    `Id_TipoDocumento` int DEFAULT NULL,
    `Nombres` varchar(100) NOT NULL,
    `Apellidos` varchar(100) NOT NULL,
    `NumeroDocumento` char(12) DEFAULT NULL,
    `PasswordHash` varchar(255) NOT NULL,
    `Email` varchar(150) DEFAULT NULL,
    `Telefono` varchar(20) DEFAULT NULL,
    `EstadoRegistro` tinyint(1) NOT NULL DEFAULT '1',
    `Fecha_Creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`Id_Usuario`),
    UNIQUE KEY `Email` (`Email`),
    UNIQUE KEY `NumeroDocumento` (`NumeroDocumento`),
    KEY `fk_usuarios_tipodoc` (`Id_TipoDocumento`),
    CONSTRAINT `fk_usuarios_tipodoc` FOREIGN KEY (`Id_TipoDocumento`) REFERENCES `tipo_documento` (`Id_TipoDocumento`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Verificar tabla especialidad
CREATE TABLE IF NOT EXISTS `especialidad` (
    `Id_Especialidad` int NOT NULL AUTO_INCREMENT,
    `NombreEspecialidad` varchar(100) NOT NULL,
    `Descripcion` varchar(255) DEFAULT NULL,
    `Tarifa` decimal(10, 2) DEFAULT NULL,
    `EstadoRegistro` tinyint(1) NOT NULL DEFAULT '1',
    `Duracion` tinyint unsigned NOT NULL DEFAULT '30',
    PRIMARY KEY (`Id_Especialidad`),
    CONSTRAINT `especialidad_chk_1` CHECK ((`Duracion` in (30, 60)))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Verificar tabla paciente_detalle
CREATE TABLE IF NOT EXISTS `paciente_detalle` (
    `Id_Usuario` int NOT NULL,
    `FechaNacimiento` date DEFAULT NULL,
    `Direccion` varchar(255) DEFAULT NULL,
    `Departamento` varchar(100) DEFAULT NULL,
    `Provincia` varchar(100) DEFAULT NULL,
    `Distrito` varchar(100) DEFAULT NULL,
    PRIMARY KEY (`Id_Usuario`),
    CONSTRAINT `paciente_detalle_ibfk_1` FOREIGN KEY (`Id_Usuario`) REFERENCES `usuarios` (`Id_Usuario`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Verificar tabla medico_detalle
CREATE TABLE IF NOT EXISTS `medico_detalle` (
    `Id_Usuario` int NOT NULL,
    `CMP` varchar(20) DEFAULT NULL,
    `Id_Especialidad` int DEFAULT NULL,
    `Horas_Semanales` int NOT NULL DEFAULT '0',
    PRIMARY KEY (`Id_Usuario`),
    KEY `Id_Especialidad` (`Id_Especialidad`),
    CONSTRAINT `medico_detalle_ibfk_1` FOREIGN KEY (`Id_Usuario`) REFERENCES `usuarios` (`Id_Usuario`),
    CONSTRAINT `medico_detalle_ibfk_2` FOREIGN KEY (`Id_Especialidad`) REFERENCES `especialidad` (`Id_Especialidad`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Verificar tabla roles_perfil
CREATE TABLE IF NOT EXISTS `roles_perfil` (
    `Id_Perfil` int NOT NULL AUTO_INCREMENT,
    `Nombre` varchar(50) DEFAULT NULL,
    `Descripcion` varchar(255) DEFAULT NULL,
    `EstadoRegistro` tinyint(1) NOT NULL DEFAULT '1',
    PRIMARY KEY (`Id_Perfil`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Verificar tabla usuario_perfil
CREATE TABLE IF NOT EXISTS `usuario_perfil` (
    `Id_Usuario` int NOT NULL,
    `Id_Perfil` int NOT NULL,
    PRIMARY KEY (`Id_Usuario`, `Id_Perfil`),
    KEY `Id_Perfil` (`Id_Perfil`),
    CONSTRAINT `usuario_perfil_ibfk_1` FOREIGN KEY (`Id_Usuario`) REFERENCES `usuarios` (`Id_Usuario`),
    CONSTRAINT `usuario_perfil_ibfk_2` FOREIGN KEY (`Id_Perfil`) REFERENCES `roles_perfil` (`Id_Perfil`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Verificar tabla permissions
CREATE TABLE IF NOT EXISTS `permissions` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `name` varchar(255) NOT NULL,
    `description` varchar(500) DEFAULT NULL,
    `action_key` varchar(100) NOT NULL,
    `estado_registro` tinyint(1) NOT NULL DEFAULT '1',
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`),
    UNIQUE KEY `action_key` (`action_key`),
    KEY `idx_action_key` (`action_key`),
    KEY `idx_name` (`name`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Verificar tabla roles_perfil_permisos
CREATE TABLE IF NOT EXISTS `roles_perfil_permisos` (
    `Id_Perfil` int NOT NULL,
    `permission_id` bigint NOT NULL,
    PRIMARY KEY (`Id_Perfil`, `permission_id`),
    KEY `fk_roles_perfil_permisos_permiso` (`permission_id`),
    CONSTRAINT `fk_roles_perfil_permisos_permiso` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_roles_perfil_permisos_rol` FOREIGN KEY (`Id_Perfil`) REFERENCES `roles_perfil` (`Id_Perfil`) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Verificar tabla horario
CREATE TABLE IF NOT EXISTS `horario` (
    `id_horario` int NOT NULL AUTO_INCREMENT,
    `dia_semana` enum(
        'Lunes',
        'Martes',
        'Miércoles',
        'Jueves',
        'Viernes',
        'Sábado'
    ) NOT NULL COMMENT 'Día de la semana laboral',
    `hora_inicio` time NOT NULL,
    `hora_fin` time NOT NULL,
    PRIMARY KEY (`id_horario`),
    CONSTRAINT `chk_hora_valida` CHECK ((`hora_inicio` < `hora_fin`))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Verificar tabla medico_horario
CREATE TABLE IF NOT EXISTS `medico_horario` (
    `id_medico_horario` int NOT NULL AUTO_INCREMENT,
    `id_usuario` int NOT NULL COMMENT 'FK a usuarios (médico)',
    `id_horario` int NOT NULL COMMENT 'FK a horario (turno asignado)',
    `estado` enum('ACTIVO', 'INACTIVO') NOT NULL DEFAULT 'ACTIVO' COMMENT 'Estado del vínculo del médico con el horario',
    PRIMARY KEY (`id_medico_horario`),
    UNIQUE KEY `uk_medico_horario` (`id_usuario`, `id_horario`),
    KEY `idx_medico` (`id_usuario`),
    KEY `idx_horario` (`id_horario`),
    KEY `idx_estado` (`estado`),
    KEY `idx_medico_horario_activo` (`id_usuario`, `estado`),
    CONSTRAINT `fk_medico_horario_horario` FOREIGN KEY (`id_horario`) REFERENCES `horario` (`id_horario`) ON DELETE CASCADE,
    CONSTRAINT `fk_medico_horario_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`Id_Usuario`) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Verificar tabla cita
CREATE TABLE IF NOT EXISTS `cita` (
    `id_cita` int NOT NULL AUTO_INCREMENT,
    `id_medico_horario` int NOT NULL,
    `id_usuario` int NOT NULL COMMENT 'FK a usuarios (paciente)',
    `fecha` date NOT NULL COMMENT 'Fecha programada de la cita',
    `hora` time NOT NULL COMMENT 'Hora programada de la cita',
    `estado` enum(
        'RESERVADA',
        'CANCELADA',
        'COMPLETADA',
        'NO_ATENDIDA'
    ) NOT NULL DEFAULT 'RESERVADA' COMMENT 'Estado actual de la cita',
    `motivo` varchar(500) DEFAULT NULL COMMENT 'Motivo o descripción breve de la cita',
    `fecha_creacion` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha y hora de creación del registro',
    `fecha_actualizacion` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Última fecha de modificación del registro',
    PRIMARY KEY (`id_cita`),
    KEY `idx_medico_horario` (`id_medico_horario`),
    KEY `idx_paciente` (`id_usuario`),
    KEY `idx_fecha` (`fecha`),
    KEY `idx_estado` (`estado`),
    KEY `idx_cita_fecha_hora` (`fecha`, `hora`),
    CONSTRAINT `fk_cita_medico_horario` FOREIGN KEY (`id_medico_horario`) REFERENCES `medico_horario` (`id_medico_horario`) ON DELETE CASCADE,
    CONSTRAINT `fk_cita_paciente` FOREIGN KEY (`id_usuario`) REFERENCES `usuarios` (`Id_Usuario`) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Verificar tabla opcionmenu
CREATE TABLE IF NOT EXISTS `opcionmenu` (
    `Id_OpcionMenu` int NOT NULL AUTO_INCREMENT,
    `Nombre` varchar(100) DEFAULT NULL,
    `UrlMenu` varchar(100) DEFAULT NULL,
    `Descripcion` varchar(255) DEFAULT NULL,
    `Id_Padre` int DEFAULT NULL,
    `EstadoRegistro` tinyint(1) NOT NULL DEFAULT '1',
    PRIMARY KEY (`Id_OpcionMenu`),
    KEY `Id_Padre` (`Id_Padre`),
    CONSTRAINT `opcionmenu_ibfk_1` FOREIGN KEY (`Id_Padre`) REFERENCES `opcionmenu` (`Id_OpcionMenu`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Verificar tabla token_sesion
CREATE TABLE IF NOT EXISTS `token_sesion` (
    `Id_Token` int NOT NULL AUTO_INCREMENT,
    `Id_Usuario` int DEFAULT NULL,
    `Token` varchar(100) DEFAULT NULL,
    `FechaExpiracion` datetime DEFAULT NULL,
    `Activo` tinyint(1) NOT NULL DEFAULT '1',
    PRIMARY KEY (`Id_Token`),
    KEY `Id_Usuario` (`Id_Usuario`),
    CONSTRAINT `token_sesion_ibfk_1` FOREIGN KEY (`Id_Usuario`) REFERENCES `usuarios` (`Id_Usuario`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Verificar tabla intento_login
CREATE TABLE IF NOT EXISTS `intento_login` (
    `Id_Intento` int NOT NULL AUTO_INCREMENT,
    `Id_Usuario` int DEFAULT NULL,
    `Token` varchar(100) DEFAULT NULL,
    `FechaIntento` datetime DEFAULT NULL,
    `Exitoso` tinyint(1) NOT NULL DEFAULT '0',
    `IpOrigen` varchar(50) DEFAULT NULL,
    PRIMARY KEY (`Id_Intento`),
    KEY `Id_Usuario` (`Id_Usuario`),
    CONSTRAINT `intento_login_ibfk_1` FOREIGN KEY (`Id_Usuario`) REFERENCES `usuarios` (`Id_Usuario`)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;