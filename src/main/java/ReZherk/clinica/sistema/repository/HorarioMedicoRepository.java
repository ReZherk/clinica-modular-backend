package ReZherk.clinica.sistema.repository;

import ReZherk.clinica.sistema.entity.HorarioMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorarioMedicoRepository extends JpaRepository<HorarioMedico, Integer> {
 List<HorarioMedico> findByMedico_Id(Integer idMedico); // findByMedico.idUsuario si tu campo se llama idUsuario

 List<HorarioMedico> findByMedico_IdAndDiaSemana(Integer idMedico, String diaSemana);
}
