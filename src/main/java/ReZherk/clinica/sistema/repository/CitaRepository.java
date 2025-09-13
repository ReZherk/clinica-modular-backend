package ReZherk.clinica.sistema.repository;

import ReZherk.clinica.sistema.entity.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {

 @Query("SELECT c FROM Cita c WHERE c.paciente.idUsuario = :idPaciente")
 List<Cita> findByPacienteId(@Param("idPaciente") Integer idPaciente);

 @Query("SELECT c FROM Cita c WHERE c.medico.idUsuario = :idMedico")
 List<Cita> findByMedicoId(@Param("idMedico") Integer idMedico);

 @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cita c WHERE c.medico.idUsuario = :idMedico AND c.fechaHora = :fechaHora AND c.estado = true")
 boolean existsActiveByMedicoIdAndFechaHora(@Param("idMedico") Integer idMedico,
   @Param("fechaHora") LocalDateTime fechaHora);

 @Query("SELECT c FROM Cita c WHERE c.paciente.idUsuario = :idPaciente AND c.fechaHora >= :from ORDER BY c.fechaHora")
 List<Cita> findFutureByPaciente(@Param("idPaciente") Integer idPaciente, @Param("from") LocalDateTime from);
}