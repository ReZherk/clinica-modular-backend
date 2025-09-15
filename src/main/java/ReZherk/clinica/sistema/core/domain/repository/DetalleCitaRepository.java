package ReZherk.clinica.sistema.core.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ReZherk.clinica.sistema.core.domain.entity.DetalleCita;

import java.util.List;

@Repository
public interface DetalleCitaRepository extends JpaRepository<DetalleCita, Integer> {
 List<DetalleCita> findByCita_Id(Integer idCita);
}
