-- V25__create_sp_listar_citas.sql

DELIMITER $$

DROP PROCEDURE IF EXISTS sp_listar_citas$$

CREATE PROCEDURE sp_listar_citas(
    IN p_id_medico INT,
    IN p_id_especialidad INT,
    IN p_id_paciente INT,
    IN p_estado VARCHAR(20),
    IN p_fecha DATE,
    IN p_fecha_inicio DATE,
    IN p_fecha_fin DATE,
    IN p_page INT,
    IN p_size INT
)
BEGIN
    DECLARE v_offset INT;
    
    -- Calcular offset para paginación
    SET v_offset = p_page * p_size;
    
    -- Consulta principal con filtros opcionales
    SELECT 
        c.id_cita AS idCita,
        c.fecha AS fecha,
        c.hora AS hora,
        c.estado AS estado,
        c.motivo AS motivo,
        
        -- Datos del paciente
        u_paciente.Id_Usuario AS idPaciente,
        u_paciente.Nombres AS nombresPaciente,
        u_paciente.Apellidos AS apellidosPaciente,
        u_paciente.NumeroDocumento AS documentoPaciente,
        u_paciente.Email AS emailPaciente,
        u_paciente.Telefono AS telefonoPaciente,
        
        -- Datos del médico
        u_medico.Id_Usuario AS idMedico,
        u_medico.Nombres AS nombresMedico,
        u_medico.Apellidos AS apellidosMedico,
        md.CMP AS cmpMedico,
        
        -- Datos de la especialidad
        e.Id_Especialidad AS idEspecialidad,
        e.NombreEspecialidad AS nombreEspecialidad,
        e.Tarifa AS tarifa,
        e.Duracion AS duracion,
        
        -- Datos del horario
        h.dia_semana AS diaSemana,
        h.hora_inicio AS horaInicio,
        h.hora_fin AS horaFin,
        
        -- Total de registros (para paginación)
        COUNT(*) OVER() AS totalRegistros
        
    FROM cita c
    
    -- Joins necesarios
    INNER JOIN medico_horario mh ON c.id_medico_horario = mh.id_medico_horario
    INNER JOIN usuarios u_medico ON mh.id_usuario = u_medico.Id_Usuario
    INNER JOIN medico_detalle md ON u_medico.Id_Usuario = md.Id_Usuario
    INNER JOIN especialidad e ON md.Id_Especialidad = e.Id_Especialidad
    INNER JOIN horario h ON mh.id_horario = h.id_horario
    INNER JOIN usuarios u_paciente ON c.id_usuario = u_paciente.Id_Usuario
    
    -- Condiciones de filtrado opcionales
    WHERE 1=1
        AND (p_id_medico IS NULL OR u_medico.Id_Usuario = p_id_medico)
        AND (p_id_especialidad IS NULL OR e.Id_Especialidad = p_id_especialidad)
        AND (p_id_paciente IS NULL OR u_paciente.Id_Usuario = p_id_paciente)
        AND (p_estado IS NULL OR c.estado = p_estado)
        AND (p_fecha IS NULL OR c.fecha = p_fecha)
        AND (p_fecha_inicio IS NULL OR c.fecha >= p_fecha_inicio)
        AND (p_fecha_fin IS NULL OR c.fecha <= p_fecha_fin)
    
    -- Ordenamiento
    ORDER BY c.fecha DESC, c.hora DESC
    
    -- Paginación
    LIMIT p_size OFFSET v_offset;

END$$

DELIMITER;