package ReZherk.clinica.sistema.core.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ReZherk.clinica.sistema.core.domain.entity.DetalleCita;

import java.util.Optional;

@Repository
public interface DetalleCitaRepository extends JpaRepository<DetalleCita, Integer> {

 @Query("SELECT dc FROM DetalleCita dc WHERE dc.cita.idCita = :idCita")
 Optional<DetalleCita> findByCitaId(@Param("idCita") Integer idCita);

 boolean existsByCitaIdCita(Integer idCita);

}
