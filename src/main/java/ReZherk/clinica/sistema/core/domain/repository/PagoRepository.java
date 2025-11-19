package ReZherk.clinica.sistema.core.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ReZherk.clinica.sistema.core.domain.entity.Pago;
import ReZherk.clinica.sistema.core.shared.enums.EstadoPago;

import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {

 @Query("SELECT p FROM Pago p " +
   "JOIN FETCH p.cita c " +
   "JOIN FETCH c.medicoHorario mh " +
   "JOIN FETCH mh.medico " +
   "WHERE p.cita.idCita = :idCita")
 Optional<Pago> findByCitaId(@Param("idCita") Integer idCita);

 Optional<Pago> findByNumeroReferencia(String numeroReferencia);

 @Query("SELECT p FROM Pago p WHERE p.paciente.id = :idPaciente ORDER BY p.fechaPago DESC")
 Page<Pago> findByPacienteId(@Param("idPaciente") Integer idPaciente, Pageable pageable);

 boolean existsByCitaIdCitaAndEstadoPago(Integer idCita, EstadoPago estadoPago);

}
