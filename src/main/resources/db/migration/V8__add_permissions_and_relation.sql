-- ==========================================
-- Migration V8: Creación de tabla 'permissions' y relación con 'roles_perfil'
-- Autor: Patrick Alcántara
-- Fecha: 2025-09-22
-- Descripción: Define permisos específicos del sistema
--              y la relación muchos-a-muchos con roles.
-- ==========================================

-- 1️⃣ Crear tabla 'permissions'
CREATE TABLE IF NOT EXISTS permissions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

-- Nombre descriptivo del permiso (Ej: "Lectura de Usuarios")
name VARCHAR(255) NOT NULL UNIQUE,

-- Descripción funcional del permiso
description VARCHAR(500),

-- Clave de acción usada en @PreAuthorize (Ej: "USER_READ")
action_key VARCHAR(100) NOT NULL UNIQUE,

-- Estado lógico (1 = activo, 0 = inactivo)
estado_registro TINYINT(1) NOT NULL DEFAULT 1,

-- Índices para búsquedas rápidas
INDEX idx_action_key (action_key), INDEX idx_name (name) );

-- 2️⃣ Crear tabla intermedia para relación muchos-a-muchos
CREATE TABLE IF NOT EXISTS roles_perfil_permisos (
    Id_Perfil INT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (Id_Perfil, permission_id),
    CONSTRAINT fk_roles_perfil_permisos_rol FOREIGN KEY (Id_Perfil) REFERENCES roles_perfil (Id_Perfil) ON DELETE CASCADE,
    CONSTRAINT fk_roles_perfil_permisos_permiso FOREIGN KEY (permission_id) REFERENCES permissions (id) ON DELETE CASCADE
);