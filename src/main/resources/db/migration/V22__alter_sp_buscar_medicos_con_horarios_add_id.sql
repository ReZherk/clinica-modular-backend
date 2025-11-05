DROP PROCEDURE IF EXISTS sp_buscar_medicos_con_horarios;

DELIMITER $$

CREATE PROCEDURE sp_buscar_medicos_con_horarios(
    IN p_nombre VARCHAR(200),
    IN p_dni VARCHAR(12),
    IN p_cmp VARCHAR(20),
    IN p_especialidad VARCHAR(100),
    IN p_page INT,
    IN p_size INT
)
BEGIN
    DECLARE v_offset INT;
    SET v_offset = p_page * p_size;

    -- Consulta principal con los horarios agrupados
    SELECT 
        u.Id_Usuario,
        u.Nombres,
        u.Apellidos,
        CONCAT(u.Nombres, ' ', u.Apellidos) AS nombre_completo,
        u.NumeroDocumento AS dni,
        u.Email,
        u.Telefono,
        md.CMP,
        e.Id_Especialidad,
        e.NombreEspecialidad AS especialidad,
        e.Duracion AS duracion_consulta,
        md.Horas_Semanales,
        GROUP_CONCAT(
            DISTINCT CONCAT(
                h.id_horario, ':',
                h.dia_semana, ':',
                TIME_FORMAT(h.hora_inicio, '%H:%i'), '-',
                TIME_FORMAT(h.hora_fin, '%H:%i')
            )
            ORDER BY 
                FIELD(h.dia_semana, 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'),
                h.hora_inicio
            SEPARATOR '|'
        ) AS horarios,
        COUNT(DISTINCT mh.id_medico_horario) AS total_bloques_horarios
    FROM usuarios u
    INNER JOIN medico_detalle md ON u.Id_Usuario = md.Id_Usuario
    INNER JOIN especialidad e ON md.Id_Especialidad = e.Id_Especialidad
    INNER JOIN medico_horario mh ON u.Id_Usuario = mh.id_usuario
    INNER JOIN horario h ON mh.id_horario = h.id_horario
    WHERE 
        u.EstadoRegistro = 1
        AND mh.estado = 'ACTIVO'
        AND (p_nombre IS NULL OR CONCAT(u.Nombres, ' ', u.Apellidos) LIKE CONCAT('%', p_nombre, '%'))
        AND (p_dni IS NULL OR u.NumeroDocumento = p_dni)
        AND (p_cmp IS NULL OR md.CMP = p_cmp)
        AND (p_especialidad IS NULL OR e.NombreEspecialidad LIKE CONCAT('%', p_especialidad, '%'))
    GROUP BY 
        u.Id_Usuario, u.Nombres, u.Apellidos, u.NumeroDocumento, u.Email, u.Telefono,
        md.CMP, e.Id_Especialidad, e.NombreEspecialidad, e.Duracion, md.Horas_Semanales
    ORDER BY u.Apellidos, u.Nombres
    LIMIT p_size OFFSET v_offset;

    -- Total de registros
    SELECT COUNT(DISTINCT u.Id_Usuario) AS total
    FROM usuarios u
    INNER JOIN medico_detalle md ON u.Id_Usuario = md.Id_Usuario
    INNER JOIN especialidad e ON md.Id_Especialidad = e.Id_Especialidad
    INNER JOIN medico_horario mh ON u.Id_Usuario = mh.id_usuario
    INNER JOIN horario h ON mh.id_horario = h.id_horario
    WHERE 
        u.EstadoRegistro = 1
        AND mh.estado = 'ACTIVO'
        AND (p_nombre IS NULL OR CONCAT(u.Nombres, ' ', u.Apellidos) LIKE CONCAT('%', p_nombre, '%'))
        AND (p_dni IS NULL OR u.NumeroDocumento = p_dni)
        AND (p_cmp IS NULL OR md.CMP = p_cmp)
        AND (p_especialidad IS NULL OR e.NombreEspecialidad LIKE CONCAT('%', p_especialidad, '%'));
END$$

DELIMITER;