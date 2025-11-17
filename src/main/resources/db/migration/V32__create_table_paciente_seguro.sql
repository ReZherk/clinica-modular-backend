-- ============================================================================
-- Tabla: paciente_seguro
-- ============================================================================
-- Descripcion: Relacion entre pacientes y sus seguros medicos.
--              Permite que un paciente tenga vinculado un seguro con su poliza.
--
-- Campos principales:
--   - id_usuario: FK al paciente
--   - id_seguro: FK al seguro
--   - numero_poliza: Numero de poliza del seguro
--   - fecha_vigencia_inicio/fin: Periodo de vigencia
--   - estado_activo: Si la poliza esta activa
-- ============================================================================

CREATE TABLE IF NOT EXISTS paciente_seguro (
    id_paciente_seguro INT NOT NULL AUTO_INCREMENT,
    id_usuario INT NOT NULL COMMENT 'FK a usuarios (paciente)',
    id_seguro INT NOT NULL COMMENT 'FK a seguro',
    numero_poliza VARCHAR(50) COMMENT 'Numero de poliza del seguro',
    fecha_vigencia_inicio DATE COMMENT 'Inicio de vigencia de la poliza',
    fecha_vigencia_fin DATE COMMENT 'Fin de vigencia de la poliza',
    estado_activo TINYINT(1) NOT NULL DEFAULT 1 COMMENT 'Si la poliza esta activa',
    fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_paciente_seguro),
    KEY idx_usuario (id_usuario),
    KEY idx_seguro (id_seguro),
    KEY idx_vigencia (
        fecha_vigencia_inicio,
        fecha_vigencia_fin
    ),
    KEY idx_estado (estado_activo),
    CONSTRAINT fk_paciente_seguro_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios (Id_Usuario) ON DELETE CASCADE,
    CONSTRAINT fk_paciente_seguro_seguro FOREIGN KEY (id_seguro) REFERENCES seguro (id_seguro) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;