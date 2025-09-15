package ReZherk.clinica.sistema.core.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ReZherk.clinica.sistema.core.domain.entity.Especialidad;

import java.util.List;

@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Integer> {

 List<Especialidad> findAllByOrderByNombreEspecialidad();

 boolean existsByNombreEspecialidad(String nombreEspecialidad);
}
