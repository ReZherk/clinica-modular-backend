package ReZherk.clinica.sistema.repository;

import ReZherk.clinica.sistema.entity.HorarioMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorarioMedicoRepository extends JpaRepository<HorarioMedico, Integer> {

 List<HorarioMedico> findByMedico_IdUsuario(Integer idMedico);

 List<HorarioMedico> findByMedico_IdUsuarioAndDiaSemana(Integer idMedico, String diaSemana);
}