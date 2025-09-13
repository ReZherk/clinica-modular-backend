package ReZherk.clinica.sistema.repository;

import ReZherk.clinica.sistema.entity.Tarifario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TarifarioRepository extends JpaRepository<Tarifario, Integer> {
 List<Tarifario> findByEspecialidad_IdAndActivoTrue(Integer especialidadId);
}