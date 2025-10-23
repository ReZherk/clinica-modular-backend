-- =============================================
-- Flyway Migration V15
-- Descripción: Eliminación de tablas obsoletas 'opcionesMenu' y 'Perfil_opcionMenu'
-- Autor: Sistema
-- Fecha: 2025-10-22
-- =============================================

-- Eliminar tabla 'opcionesMenu' si existe
DROP TABLE IF EXISTS `opcionesMenu`;

-- Eliminar tabla 'Perfil_opcionMenu' si existe
DROP TABLE IF EXISTS `Perfil_opcionMenu`;

-- =============================================
-- Fin de migración V15
-- =============================================