ALTER TABLE especialidad
ADD COLUMN EstadoRegistro TINYINT(1) NOT NULL DEFAULT 1 AFTER Tarifa;

-- Opcional: actualizar registros existentes para que todos estén activos al inicio
UPDATE especialidad SET EstadoRegistro = 1;