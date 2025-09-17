-- ===================================
-- V5__add_tarifa_to_especialidad.sql
-- Descripción: Añadir columna Tarifa a la tabla especialidad
-- ===================================

USE clinica_db;

ALTER TABLE especialidad
ADD COLUMN Tarifa DECIMAL(10, 2) NULL AFTER Descripcion;