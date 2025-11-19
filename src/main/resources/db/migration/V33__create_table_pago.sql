-- ============================================================================
-- Tabla: pago
-- ============================================================================
-- Descripcion: Registra todas las transacciones de pago de citas medicas.
--              Soporta pagos con tarjeta, Yape o cubiertos por seguro.
--
-- Campos principales:
--   - id_cita: Cita asociada al pago
--   - monto: Monto total pagado
--   - metodo_pago: TARJETA, YAPE o SEGURO
--   - estado_pago: PENDIENTE, COMPLETADO, FALLIDO, CANCELADO
--   - datos_pago: JSON con detalles especificos del metodo
--   - numero_referencia: Codigo unico de transaccion
-- ============================================================================

CREATE TABLE IF NOT EXISTS pago (
    id_pago INT NOT NULL AUTO_INCREMENT,
    id_cita INT NOT NULL COMMENT 'FK a cita',
    id_usuario INT NOT NULL COMMENT 'FK a usuarios (paciente)',
    monto DECIMAL(10, 2) NOT NULL COMMENT 'Monto total del pago',
    metodo_pago ENUM('TARJETA', 'YAPE', 'SEGURO') NOT NULL COMMENT 'Metodo de pago utilizado',
    estado_pago ENUM(
        'PENDIENTE',
        'COMPLETADO',
        'FALLIDO',
        'CANCELADO'
    ) NOT NULL DEFAULT 'PENDIENTE',
    numero_referencia VARCHAR(100) COMMENT 'Codigo unico de transaccion',
    fecha_pago DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion DATETIME NULL ON UPDATE CURRENT_TIMESTAMP,
    datos_pago JSON COMMENT 'Detalles del pago segun metodo: tarjeta, yape o seguro',
    id_paciente_seguro INT NULL COMMENT 'FK a paciente_seguro si aplica',
    PRIMARY KEY (id_pago),
    UNIQUE KEY uk_numero_referencia (numero_referencia),
    KEY idx_cita (id_cita),
    KEY idx_usuario (id_usuario),
    KEY idx_estado (estado_pago),
    KEY idx_metodo (metodo_pago),
    KEY idx_fecha (fecha_pago),
    KEY idx_referencia (numero_referencia),
    CONSTRAINT fk_pago_cita FOREIGN KEY (id_cita) REFERENCES cita (id_cita) ON DELETE CASCADE,
    CONSTRAINT fk_pago_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios (Id_Usuario) ON DELETE CASCADE,
    CONSTRAINT fk_pago_paciente_seguro FOREIGN KEY (id_paciente_seguro) REFERENCES paciente_seguro (id_paciente_seguro) ON DELETE SET NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci;

-- Agregar campo id_pago a la tabla cita
ALTER TABLE cita
ADD COLUMN id_pago INT NULL COMMENT 'FK a pago (opcional)' AFTER motivo,
ADD KEY idx_pago (id_pago),
ADD CONSTRAINT fk_cita_pago FOREIGN KEY (id_pago) REFERENCES pago (id_pago) ON DELETE SET NULL;