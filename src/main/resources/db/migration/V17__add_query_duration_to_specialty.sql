-- =============================================
-- Flyway Migration V17
-- Descripción: Agregar columna 'Duracion' a la tabla 'especialidad'
-- Autor: Sistema
-- Fecha: 2025-10-23
-- =============================================

ALTER TABLE `especialidad`
ADD COLUMN `Duracion` TINYINT UNSIGNED NOT NULL DEFAULT 30 CHECK (`Duracion` IN (30, 60));

-- =============================================
-- Fin de migración V17
-- =============================================