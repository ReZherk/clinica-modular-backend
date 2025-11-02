package ReZherk.clinica.sistema.core.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ReZherk.clinica.sistema.core.domain.entity.Cita;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {

 @Query("SELECT c FROM Cita c " +
   "WHERE c.medicoHorario.medico.idUsuario = :idMedico " +
   "AND c.fecha = :fecha " +
   "AND c.estado = 'RESERVADA'")
 List<Cita> findByMedicoAndFechaAndEstadoReservada(
   @Param("idMedico") Integer idMedico,
   @Param("fecha") LocalDate fecha);

 @Query("SELECT c FROM Cita c " +
   "WHERE c.paciente.idUsuario = :idPaciente " +
   "AND c.estado IN ('RESERVADA', 'COMPLETADA') " +
   "ORDER BY c.fecha DESC, c.hora DESC")
 List<Cita> findByPacienteOrderByFechaDesc(@Param("idPaciente") Integer idPaciente);

 @Query("SELECT COUNT(c) > 0 FROM Cita c " +
   "WHERE c.medicoHorario.idMedicoHorario = :idMedicoHorario " +
   "AND c.fecha = :fecha " +
   "AND c.hora = :hora " +
   "AND c.estado = 'RESERVADA'")
 boolean existsCitaReservada(
   @Param("idMedicoHorario") Integer idMedicoHorario,
   @Param("fecha") LocalDate fecha,
   @Param("hora") LocalTime hora);
}
