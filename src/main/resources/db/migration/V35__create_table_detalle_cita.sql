-- ============================================================================
-- Tabla: detalle_cita
-- ============================================================================
-- Descripcion: Almacena la informacion medica registrada por el medico
--              durante o despues de la cita (diagnostico, receta, observaciones).
--
-- Campos principales:
--   - id_cita: FK a cita (relacion uno a uno)
--   - diagnostico: Diagnostico medico de la consulta
--   - receta: Prescripcion medica o receta
--   - observaciones: Observaciones adicionales del medico
--   - fecha_registro: Cuando se registro la informacion
-- ============================================================================

CREATE TABLE IF NOT EXISTS detalle_cita (
    id_detalle_cita INT NOT NULL AUTO_INCREMENT,
    id_cita INT NOT NULL COMMENT 'FK a cita',
    diagnostico TEXT COMMENT 'Diagnostico medico',
    receta TEXT COMMENT 'Receta o prescripcion medica',
    observaciones TEXT COMMENT 'Observaciones adicionales',
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id_detalle_cita),
    UNIQUE KEY uk_cita (id_cita),
    CONSTRAINT fk_detalle_cita_cita FOREIGN KEY (id_cita) REFERENCES cita (id_cita) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;