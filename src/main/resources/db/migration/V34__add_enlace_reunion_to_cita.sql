-- ============================================================================
-- Modificacion: Agregar columna enlace_reunion a tabla cita
-- ============================================================================
-- Descripcion: Agrega campo para almacenar el enlace de videollamada o reunion
--              virtual para las citas medicas.
--
-- Campo agregado:
--   - enlace_reunion: URL de la reunion virtual (Google Meet, Zoom, etc)
-- ============================================================================

ALTER TABLE cita
ADD COLUMN enlace_reunion VARCHAR(500) NULL COMMENT 'Enlace de la reunion virtual' AFTER motivo;

-- Agregar indice para busquedas por enlace
ALTER TABLE cita ADD KEY idx_enlace_reunion (enlace_reunion (255));