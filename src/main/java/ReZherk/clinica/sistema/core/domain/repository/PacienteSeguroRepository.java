package ReZherk.clinica.sistema.core.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ReZherk.clinica.sistema.core.domain.entity.PacienteSeguro;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PacienteSeguroRepository extends JpaRepository<PacienteSeguro, Integer> {

 @Query("SELECT ps FROM PacienteSeguro ps " +
   "JOIN FETCH ps.seguro " +
   "WHERE ps.paciente.id = :idPaciente " +
   "AND ps.estadoActivo = true " +
   "AND (ps.fechaVigenciaInicio IS NULL OR ps.fechaVigenciaInicio <= :fecha) " +
   "AND (ps.fechaVigenciaFin IS NULL OR ps.fechaVigenciaFin >= :fecha)")
 Optional<PacienteSeguro> findSeguroVigente(@Param("idPaciente") Integer idPaciente, @Param("fecha") LocalDate fecha);

 Optional<PacienteSeguro> findByPacienteIdAndEstadoActivoTrue(Integer idPaciente);
}
