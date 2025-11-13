-- V29__add_dni_medico_to_sp_listar_citas.sql

-- ============================================================================
-- Procedimiento: sp_listar_citas
-- ============================================================================
-- Descripcion: Lista todas las citas con filtros opcionales y paginacion.
--              Ahora incluye el DNI del medico en los resultados.
--
-- Parametros de entrada:
--   - p_cmp_medico: Filtro por CMP del medico (busqueda parcial)
--   - p_dni_medico: Filtro por DNI del medico (busqueda parcial)
--   - p_nombre_medico: Filtro por nombre completo del medico
--   - p_id_especialidad: Filtro por ID de especialidad
--   - p_estado: Filtro por estado de cita (RESERVADA, CANCELADA, COMPLETADA, NO_ATENDIDA)
--   - p_fecha: Filtro por fecha especifica
--   - p_fecha_inicio: Filtro por rango de fechas (inicio)
--   - p_fecha_fin: Filtro por rango de fechas (fin)
--   - p_page: Numero de pagina (comienza en 0)
--   - p_size: Cantidad de registros por pagina
--
-- Retorna:
--   - Datos completos de la cita (id, fecha, hora, estado, motivo)
--   - Datos del paciente (id, nombres, apellidos, documento, email, telefono)
--   - Datos del medico (id, nombres, apellidos, DNI, CMP)
--   - Datos de la especialidad (id, nombre, tarifa, duracion)
--   - Datos del horario (dia, hora inicio calculada, hora fin calculada)
--   - Total de registros (para paginacion)
--
-- Notas:
--   - Todos los parametros son opcionales (pueden ser NULL)
--   - La hora de fin se calcula automaticamente: horaInicio + duracion especialidad
--   - Los filtros se aplican con AND logico
-- ============================================================================

DELIMITER $$

DROP PROCEDURE IF EXISTS sp_listar_citas$$

CREATE PROCEDURE sp_listar_citas(
    IN p_cmp_medico VARCHAR(20),
    IN p_dni_medico VARCHAR(12),
    IN p_nombre_medico VARCHAR(100),
    IN p_id_especialidad INT,
    IN p_estado VARCHAR(20),
    IN p_fecha DATE,
    IN p_fecha_inicio DATE,
    IN p_fecha_fin DATE,
    IN p_page INT,
    IN p_size INT
)
BEGIN
    DECLARE v_offset INT;
    
    -- Calcular offset para paginacion
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
        
        -- Datos del medico
        u_medico.Id_Usuario AS idMedico,
        u_medico.Nombres AS nombresMedico,
        u_medico.Apellidos AS apellidosMedico,
        u_medico.NumeroDocumento AS dniMedico,
        md.CMP AS cmpMedico,
        
        -- Datos de la especialidad
        e.Id_Especialidad AS idEspecialidad,
        e.NombreEspecialidad AS nombreEspecialidad,
        e.Tarifa AS tarifa,
        e.Duracion AS duracion,
        
        -- Hora de inicio y fin de la cita
        h.dia_semana AS diaSemana,
        c.hora AS horaInicio,
        ADDTIME(c.hora, SEC_TO_TIME(e.Duracion * 60)) AS horaFin,
        
        -- Total de registros para paginacion
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
        AND (p_cmp_medico IS NULL OR md.CMP LIKE CONCAT('%', p_cmp_medico, '%'))
        AND (p_dni_medico IS NULL OR u_medico.NumeroDocumento LIKE CONCAT('%', p_dni_medico, '%'))
        AND (p_nombre_medico IS NULL OR CONCAT(u_medico.Nombres, ' ', u_medico.Apellidos) LIKE CONCAT('%', p_nombre_medico, '%'))
        AND (p_id_especialidad IS NULL OR e.Id_Especialidad = p_id_especialidad)
        AND (p_estado IS NULL OR c.estado = p_estado)
        AND (p_fecha IS NULL OR c.fecha = p_fecha)
        AND (p_fecha_inicio IS NULL OR c.fecha >= p_fecha_inicio)
        AND (p_fecha_fin IS NULL OR c.fecha <= p_fecha_fin)
    
    -- Ordenamiento
    ORDER BY c.fecha DESC, c.hora DESC
    
    -- Paginacion
    LIMIT p_size OFFSET v_offset;

END$$

DELIMITER;