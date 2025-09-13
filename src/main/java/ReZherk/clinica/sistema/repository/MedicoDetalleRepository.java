package ReZherk.clinica.sistema.repository;

import ReZherk.clinica.sistema.entity.MedicoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public interface MedicoDetalleRepository extends JpaRepository<MedicoDetalle, Integer> {
 // Lista simples de médicos por especialidad (activos)
 List<MedicoDetalle> findByEspecialidad_IdAndUsuario_EstadoRegistroTrue(Integer especialidadId);

 /**
  * JPQL: médicos de una especialidad que:
  * - tienen estadoRegistro = true
  * - tienen horario en ese diaSemana y la hora dada cae entre horaInicio/horaFin
  * - no tienen ya una cita activa en la fechaHora exacta
  *
  * Parámetros:
  * - especialidadId: id de la especialidad
  * - diaSemana: texto tal como está en la columna Horario_Medico.DiaSemana (ej:
  * "Lunes")
  * - hora: LocalTime extraído de la fecha/hora de la cita solicitada
  * - fechaHora: LocalDateTime completo de la cita (para chequear conflictos
  * exactos)
  */
 @Query("""
     SELECT md FROM MedicoDetalle md
     WHERE md.especialidad.id = :especialidadId
       AND md.usuario.estadoRegistro = true
       AND EXISTS (
         SELECT h FROM HorarioMedico h
         WHERE h.medico = md
           AND h.diaSemana = :diaSemana
           AND :hora BETWEEN h.horaInicio AND h.horaFin
       )
       AND NOT EXISTS (
         SELECT c FROM Cita c
         WHERE c.medico = md
           AND c.fechaHora = :fechaHora
           AND c.estado = true
       )
   """)
 List<MedicoDetalle> findAvailableByEspecialidadAndDiaHora(
   @Param("especialidadId") Integer especialidadId,
   @Param("diaSemana") String diaSemana,
   @Param("hora") LocalTime hora,
   @Param("fechaHora") LocalDateTime fechaHora);
}
