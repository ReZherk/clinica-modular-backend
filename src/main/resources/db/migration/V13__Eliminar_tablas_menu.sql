-- =============================================
-- Flyway Migration V13
-- Descripción: Eliminación de tablas antiguas 'opcionesMenu' y 'Perfil_opcionMenu'
-- Autor: Sistema
-- Fecha: 2025-10-13
-- =============================================

-- Verificar y eliminar tabla 'opcionesMenu' si existe
DROP TABLE IF EXISTS `opcionesMenu`;

-- Verificar y eliminar tabla 'Perfil_opcionMenu' si existe
DROP TABLE IF EXISTS `Perfil_opcionMenu`;

-- =============================================
-- Fin de migración V13
-- =============================================