package ReZherk.clinica.sistema.repository;

import ReZherk.clinica.sistema.entity.PacienteDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PacienteDetalleRepository extends JpaRepository<PacienteDetalle, Integer> {
}