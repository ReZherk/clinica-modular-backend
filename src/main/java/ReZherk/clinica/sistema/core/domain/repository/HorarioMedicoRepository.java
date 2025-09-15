package ReZherk.clinica.sistema.core.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ReZherk.clinica.sistema.core.domain.entity.HorarioMedico;

import java.util.List;

@Repository
public interface HorarioMedicoRepository extends JpaRepository<HorarioMedico, Integer> {

 List<HorarioMedico> findByMedico_IdUsuario(Integer idMedico);

 List<HorarioMedico> findByMedico_IdUsuarioAndDiaSemana(Integer idMedico, String diaSemana);
}