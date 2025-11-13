-- V27__add_no_atendida_estado_to_cita.sql

ALTER TABLE cita
MODIFY COLUMN estado ENUM(
    'RESERVADA',
    'CANCELADA',
    'COMPLETADA',
    'NO_ATENDIDA'
) NOT NULL DEFAULT 'RESERVADA' COMMENT 'Estado actual de la cita';