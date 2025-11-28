-- ============================================================================
-- Modificacion: Agregar columna foto_perfil a tabla usuarios
-- ============================================================================
-- Descripcion: Agrega campo para almacenar la ruta o nombre del archivo
--              de la foto de perfil del usuario.
--
-- Campo agregado:
--   - foto_perfil: Nombre del archivo almacenado en el servidor
-- ============================================================================

ALTER TABLE usuarios
ADD COLUMN foto_perfil VARCHAR(255) NULL COMMENT 'Nombre del archivo de foto de perfil' AFTER Telefono;

-- Agregar indice para busquedas por foto
ALTER TABLE usuarios ADD KEY idx_foto_perfil (foto_perfil);