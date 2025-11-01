-- =============================================
-- Flyway Migration V19
-- Descripción: Creación de tablas para gestión de horarios médicos
-- Autor: ReZherk
-- Fecha: 2025-10-31
-- =============================================

-- =============================================
-- Tabla: horario
-- =============================================
CREATE TABLE IF NOT EXISTS horario (
    id_horario INT NOT NULL AUTO_INCREMENT,
    dia_semana ENUM(
        'Lunes',
        'Martes',
        'Miércoles',
        'Jueves',
        'Viernes',
        'Sábado'
    ) NOT NULL COMMENT 'Día de la semana laboral',
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    PRIMARY KEY (id_horario),
    CONSTRAINT chk_hora_valida CHECK (hora_inicio < hora_fin)
) ENGINE = InnoDB;

-- =============================================
-- Tabla: medico_horario
-- =============================================
CREATE TABLE IF NOT EXISTS medico_horario (
    id_medico_horario INT NOT NULL AUTO_INCREMENT,
    id_usuario INT NOT NULL COMMENT 'FK a usuarios (médico)',
    id_horario INT NOT NULL COMMENT 'FK a horario (turno asignado)',
    estado ENUM('ACTIVO', 'INACTIVO') NOT NULL DEFAULT 'ACTIVO' COMMENT 'Estado del vínculo del médico con el horario',
    PRIMARY KEY (id_medico_horario),
    UNIQUE KEY uk_medico_horario (id_usuario, id_horario),
    KEY idx_medico (id_usuario),
    KEY idx_horario (id_horario),
    KEY idx_estado (estado),
    CONSTRAINT fk_medico_horario_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios (Id_Usuario) ON DELETE CASCADE,
    CONSTRAINT fk_medico_horario_horario FOREIGN KEY (id_horario) REFERENCES horario (id_horario) ON DELETE CASCADE
) ENGINE = InnoDB;

-- =============================================
-- Tabla: cita
-- =============================================
CREATE TABLE IF NOT EXISTS cita (
    id_cita INT NOT NULL AUTO_INCREMENT,
    id_medico_horario INT NOT NULL,
    id_usuario INT NOT NULL COMMENT 'FK a usuarios (paciente)',
    fecha DATE NOT NULL COMMENT 'Fecha programada de la cita',
    hora TIME NOT NULL COMMENT 'Hora programada de la cita',
    estado ENUM(
        'RESERVADA',
        'CANCELADA',
        'COMPLETADA'
    ) NOT NULL DEFAULT 'RESERVADA' COMMENT 'Estado actual de la cita',
    motivo VARCHAR(500) COMMENT 'Motivo o descripción breve de la cita',
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha y hora de creación del registro',
    fecha_actualizacion DATETIME NULL ON UPDATE CURRENT_TIMESTAMP COMMENT 'Última fecha de modificación del registro',
    PRIMARY KEY (id_cita),
    KEY idx_medico_horario (id_medico_horario),
    KEY idx_paciente (id_usuario),
    KEY idx_fecha (fecha),
    KEY idx_estado (estado),
    CONSTRAINT fk_cita_medico_horario FOREIGN KEY (id_medico_horario) REFERENCES medico_horario (id_medico_horario) ON DELETE CASCADE,
    CONSTRAINT fk_cita_paciente FOREIGN KEY (id_usuario) REFERENCES usuarios (Id_Usuario) ON DELETE CASCADE
) ENGINE = InnoDB;

-- =============================================
-- Índices adicionales para optimización
-- =============================================
CREATE INDEX idx_cita_fecha_hora ON cita (fecha, hora);

CREATE INDEX idx_medico_horario_activo ON medico_horario (id_usuario, estado);

-- =============================================
-- Triggers: Validación de fecha futura en citas
-- =============================================
DELIMITER $$

-- Trigger: evitar citas con fecha pasada (INSERT)
CREATE TRIGGER trg_cita_fecha_valida_insert
BEFORE INSERT ON cita
FOR EACH ROW
BEGIN
    IF NEW.fecha < CURDATE() THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'No se puede registrar una cita con fecha anterior a hoy.';
    END IF;
END$$

-- Trigger: evitar actualización de cita a fecha pasada (UPDATE)
CREATE TRIGGER trg_cita_fecha_valida_update
BEFORE UPDATE ON cita
FOR EACH ROW
BEGIN
    IF NEW.fecha < CURDATE() THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'No se puede actualizar la cita con una fecha anterior a hoy.';
    END IF;
END$$

DELIMITER;

-- =============================================
-- Fin de migración V19
-- =============================================