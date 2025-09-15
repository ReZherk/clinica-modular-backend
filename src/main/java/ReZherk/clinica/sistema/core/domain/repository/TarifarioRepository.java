package ReZherk.clinica.sistema.core.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ReZherk.clinica.sistema.core.domain.entity.Tarifario;

import java.util.List;

@Repository
public interface TarifarioRepository extends JpaRepository<Tarifario, Integer> {
 List<Tarifario> findByEspecialidad_IdAndActivoTrue(Integer especialidadId);
}