-- ============================================================================
-- Tabla: seguro
-- ============================================================================
-- Descripcion: Almacena los seguros medicos que tienen convenio con la clinica.
--              Los seguros con convenio cubren el costo total de las consultas.
--
-- Campos principales:
--   - nombre_seguro: Nombre del seguro (ej: Pacifico, Rimac, etc)
--   - cubre_costo_total: Indica si cubre el 100% del costo
--   - estado_registro: Indica si el seguro esta activo
-- ============================================================================

CREATE TABLE IF NOT EXISTS seguro (
    id_seguro INT NOT NULL AUTO_INCREMENT,
    nombre_seguro VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    cubre_costo_total TINYINT(1) NOT NULL DEFAULT 1 COMMENT 'Indica si cubre el costo completo',
    estado_registro TINYINT(1) NOT NULL DEFAULT 1 COMMENT 'Estado activo/inactivo',
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id_seguro),
    KEY idx_estado (estado_registro),
    KEY idx_nombre (nombre_seguro)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Insertar seguros de ejemplo con convenio
INSERT INTO
    seguro (
        nombre_seguro,
        descripcion,
        cubre_costo_total
    )
VALUES (
        'Pacifico Seguros',
        'Seguro medico con cobertura completa',
        1
    ),
    (
        'Rimac Seguros',
        'Cobertura total en especialidades',
        1
    ),
    (
        'La Positiva Seguros',
        'Plan de salud integral',
        1
    ),
    (
        'Mapfre Seguros',
        'Seguro con convenio empresarial',
        1
    ),
    (
        'Sanitas Peru',
        'Atencion medica completa',
        1
    );