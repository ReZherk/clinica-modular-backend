DROP PROCEDURE IF EXISTS sp_buscar_medicos_sin_horarios;

DELIMITER $$

CREATE PROCEDURE sp_buscar_medicos_sin_horarios(
    IN p_nombre VARCHAR(200),
    IN p_dni VARCHAR(12),
    IN p_cmp VARCHAR(20),
    IN p_especialidad VARCHAR(100),
    IN p_page INT,
    IN p_size INT,
    IN p_sort_by VARCHAR(50),
    IN p_sort_direction VARCHAR(4)
)
BEGIN
    DECLARE v_offset INT;
    DECLARE v_order_clause VARCHAR(100);
    
    SET v_offset = p_page * p_size;
    
    -- Construir la cláusula ORDER BY dinámicamente
    SET v_order_clause = CASE p_sort_by
        WHEN 'id' THEN CONCAT('u.Id_Usuario ', p_sort_direction)
        WHEN 'nombres' THEN CONCAT('u.Nombres ', p_sort_direction)
        WHEN 'apellidos' THEN CONCAT('u.Apellidos ', p_sort_direction)
        WHEN 'dni' THEN CONCAT('u.NumeroDocumento ', p_sort_direction)
        WHEN 'email' THEN CONCAT('u.Email ', p_sort_direction)
        WHEN 'cmp' THEN CONCAT('md.CMP ', p_sort_direction)
        WHEN 'especialidad' THEN CONCAT('e.NombreEspecialidad ', p_sort_direction)
        ELSE CONCAT('u.Apellidos ', p_sort_direction, ', u.Nombres ', p_sort_direction)
    END;

    -- Consulta principal con ordenamiento dinámico
    SET @sql = CONCAT('
        SELECT 
            u.Id_Usuario,
            u.Nombres,
            u.Apellidos,
            CONCAT(u.Nombres, '' '', u.Apellidos) AS nombre_completo,
            u.NumeroDocumento AS dni,
            u.Email,
            u.Telefono,
            md.CMP,
            e.Id_Especialidad,
            e.NombreEspecialidad AS especialidad,
            e.Duracion AS duracion_consulta,
            md.Horas_Semanales,
            NULL AS horarios,
            0 AS total_bloques_horarios
        FROM usuarios u
        INNER JOIN medico_detalle md ON u.Id_Usuario = md.Id_Usuario
        INNER JOIN especialidad e ON md.Id_Especialidad = e.Id_Especialidad
        LEFT JOIN medico_horario mh ON u.Id_Usuario = mh.id_usuario AND mh.estado = ''ACTIVO''
        WHERE 
            u.EstadoRegistro = 1
            AND mh.id_medico_horario IS NULL
            AND (? IS NULL OR CONCAT(u.Nombres, '' '', u.Apellidos) LIKE CONCAT(''%'', ?, ''%''))
            AND (? IS NULL OR u.NumeroDocumento = ?)
            AND (? IS NULL OR md.CMP = ?)
            AND (? IS NULL OR e.NombreEspecialidad LIKE CONCAT(''%'', ?, ''%''))
        ORDER BY ', v_order_clause, '
        LIMIT ', p_size, ' OFFSET ', v_offset
    );

    PREPARE stmt FROM @sql;
    SET @p_nombre = p_nombre;
    SET @p_dni = p_dni;
    SET @p_cmp = p_cmp;
    SET @p_especialidad = p_especialidad;
    
    EXECUTE stmt USING @p_nombre, @p_nombre, @p_dni, @p_dni, @p_cmp, @p_cmp, @p_especialidad, @p_especialidad;
    DEALLOCATE PREPARE stmt;

    -- Total de registros
    SELECT COUNT(DISTINCT u.Id_Usuario) AS total
    FROM usuarios u
    INNER JOIN medico_detalle md ON u.Id_Usuario = md.Id_Usuario
    INNER JOIN especialidad e ON md.Id_Especialidad = e.Id_Especialidad
    LEFT JOIN medico_horario mh ON u.Id_Usuario = mh.id_usuario AND mh.estado = 'ACTIVO'
    WHERE 
        u.EstadoRegistro = 1
        AND mh.id_medico_horario IS NULL
        AND (p_nombre IS NULL OR CONCAT(u.Nombres, ' ', u.Apellidos) LIKE CONCAT('%', p_nombre, '%'))
        AND (p_dni IS NULL OR u.NumeroDocumento = p_dni)
        AND (p_cmp IS NULL OR md.CMP = p_cmp)
        AND (p_especialidad IS NULL OR e.NombreEspecialidad LIKE CONCAT('%', p_especialidad, '%'));
END$$

DELIMITER;