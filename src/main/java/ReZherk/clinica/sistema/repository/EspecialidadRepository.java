package ReZherk.clinica.sistema.repository;

import ReZherk.clinica.sistema.entity.Especialidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Integer> {

 List<Especialidad> findAllByOrderByNombreEspecialidad();

 boolean existsByNombreEspecialidad(String nombreEspecialidad);
}
