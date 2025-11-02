package ReZherk.clinica.sistema.core.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ReZherk.clinica.sistema.core.domain.entity.Horario;
import ReZherk.clinica.sistema.core.shared.enums.DiaSemana;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HorarioRepository extends JpaRepository<Horario, Integer> {

 @Query("SELECT h FROM Horario h WHERE h.diaSemana = :diaSemana AND h.horaInicio = :horaInicio AND h.horaFin = :horaFin")
 Optional<Horario> findByDiaSemanaAndHoraInicioAndHoraFin(
   @Param("diaSemana") DiaSemana diaSemana,
   @Param("horaInicio") LocalTime horaInicio,
   @Param("horaFin") LocalTime horaFin);

 List<Horario> findByDiaSemanaOrderByHoraInicio(DiaSemana diaSemana);

 @Query("SELECT DISTINCT h.diaSemana FROM Horario h")
 List<DiaSemana> findAllDistinctDiasSemana();

}
