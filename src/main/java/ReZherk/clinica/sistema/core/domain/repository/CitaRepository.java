package ReZherk.clinica.sistema.core.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ReZherk.clinica.sistema.core.domain.entity.Cita;
import ReZherk.clinica.sistema.modules.appointment.application.dto.response.CitaListadoResult;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {

    // Verificar si existe una cita en un horario específico
    @Query("SELECT COUNT(c) > 0 FROM Cita c " +
            "WHERE c.medicoHorario.id = :idMedicoHorario " +
            "AND c.fecha = :fecha " +
            "AND c.hora = :hora " +
            "AND c.estado != 'CANCELADA'")
    boolean existsCitaByMedicoHorarioAndFechaAndHora(
            @Param("idMedicoHorario") Integer idMedicoHorario,
            @Param("fecha") LocalDate fecha,
            @Param("hora") LocalTime hora);

    // Verificar si un paciente ya tiene una cita en esa fecha/hora
    @Query("SELECT COUNT(c) > 0 FROM Cita c " +
            "WHERE c.paciente.id = :idPaciente " +
            "AND c.fecha = :fecha " +
            "AND c.hora = :hora " +
            "AND c.estado = 'RESERVADA'")
    boolean existsCitaByPacienteAndFechaAndHora(
            @Param("idPaciente") Integer idPaciente,
            @Param("fecha") LocalDate fecha,
            @Param("hora") LocalTime hora);

    // Obtener todas las citas de un paciente
    @Query("SELECT c FROM Cita c " +
            "JOIN FETCH c.medicoHorario mh " +
            "JOIN FETCH mh.medico m " +
            "JOIN FETCH m.medicoDetalle md " +
            "JOIN FETCH md.especialidad " +
            "WHERE c.paciente.id = :idPaciente " +
            "ORDER BY c.fecha DESC, c.hora DESC")
    Page<Cita> findAllByPacienteId(@Param("idPaciente") Integer idPaciente, Pageable pageable);

    // Obtener cita por ID con todas las relaciones
    @Query("SELECT c FROM Cita c " +
            "JOIN FETCH c.medicoHorario mh " +
            "JOIN FETCH mh.medico m " +
            "JOIN FETCH m.medicoDetalle md " +
            "JOIN FETCH md.especialidad " +
            "JOIN FETCH c.paciente " +
            "WHERE c.idCita = :idCita")
    Optional<Cita> findByIdWithDetails(@Param("idCita") Integer idCita);

    // Obtener citas ocupadas de un médico en una fecha específica
    @Query("SELECT c FROM Cita c " +
            "WHERE c.medicoHorario.medico.id = :idMedico " +
            "AND c.fecha = :fecha " +
            "AND c.estado = 'RESERVADA'")
    List<Cita> findCitasOcupadasByMedicoAndFecha(
            @Param("idMedico") Integer idMedico,
            @Param("fecha") LocalDate fecha);

    // Obtener citas por médico en un rango de fechas
    @Query("SELECT c FROM Cita c " +
            "WHERE c.medicoHorario.medico.id = :idMedico " +
            "AND c.fecha BETWEEN :fechaInicio AND :fechaFin " +
            "AND c.estado != 'CANCELADA'")
    List<Cita> findCitasByMedicoAndFechaRange(
            @Param("idMedico") Integer idMedico,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);

    @Query(value = "CALL sp_listar_citas(:p_id_medico, :p_id_especialidad, :p_id_paciente, " +
            ":p_estado, :p_fecha, :p_fecha_inicio, :p_fecha_fin, :p_page, :p_size)", nativeQuery = true)
    List<CitaListadoResult> listarCitasConFiltros(
            @Param("p_id_medico") Integer idMedico,
            @Param("p_id_especialidad") Integer idEspecialidad,
            @Param("p_id_paciente") Integer idPaciente,
            @Param("p_estado") String estado,
            @Param("p_fecha") LocalDate fecha,
            @Param("p_fecha_inicio") LocalDate fechaInicio,
            @Param("p_fecha_fin") LocalDate fechaFin,
            @Param("p_page") Integer page,
            @Param("p_size") Integer size);

}
