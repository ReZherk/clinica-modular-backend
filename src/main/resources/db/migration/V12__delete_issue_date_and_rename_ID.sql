-- =============================================
-- Flyway Migration V12
-- Descripción: Ajustes en la tabla usuarios - se elimina Fecha_Emision y se renombra DNI a NumeroDocumento
-- Autor: Sistema
-- Fecha: 2025-10-08
-- =============================================

-- 1. Eliminar la columna Fecha_Emision
ALTER TABLE `usuarios` DROP COLUMN `Fecha_Emision`;

-- 2. Cambiar el nombre de la columna DNI a NumeroDocumento
ALTER TABLE `usuarios`
CHANGE COLUMN `DNI` `NumeroDocumento` CHAR(12) DEFAULT NULL;

-- 3. Actualizar el índice único (antes UNIQUE KEY `DNI`)
ALTER TABLE `usuarios`
DROP INDEX `DNI`,
ADD UNIQUE KEY `NumeroDocumento` (`NumeroDocumento`);

-- =============================================
-- Fin de migración V12
-- =============================================