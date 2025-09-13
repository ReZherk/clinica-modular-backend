package ReZherk.clinica.sistema.repository;

import ReZherk.clinica.sistema.entity.DetalleCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetalleCitaRepository extends JpaRepository<DetalleCita, Integer> {
 List<DetalleCita> findByCita_Id(Integer idCita);
}
