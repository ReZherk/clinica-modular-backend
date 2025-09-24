-- ==========================================
-- Migration 9 - Eliminar columna Salt de usuarios
-- ==========================================

ALTER TABLE usuarios DROP COLUMN Salt;