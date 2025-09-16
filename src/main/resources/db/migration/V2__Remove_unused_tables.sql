-- ===================================
-- Flyway Migration V2
-- Description: Verificación de tablas principales
-- Author: Patrick
-- Date: 2025-09-16
-- ===================================

-- Verificar que las tablas principales existan en el esquema actual
SELECT table_name
FROM information_schema.tables
WHERE
    table_schema = 'clinica_db'
    AND table_name IN (
        'usuarios',
        'paciente_detalle',
        'especialidad',
        'intento_login',
        'medico_detalle',
        'opcionmenu',
        'roles_perfil',
        'token_sesion',
        'usuario_perfil'
    );

-- Nota:
-- - No se elimina nada (eso ya lo hiciste manualmente).
-- - No se incluye flyway_schema_history porque Flyway lo administra.
-- - Este script sirve para que Flyway registre la migración en el historial
--   y quede constancia del estado esperado del esquema.