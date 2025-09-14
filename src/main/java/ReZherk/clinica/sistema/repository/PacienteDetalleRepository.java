package ReZherk.clinica.sistema.repository;

import ReZherk.clinica.sistema.entity.PacienteDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteDetalleRepository extends JpaRepository<PacienteDetalle, Integer> {

 Optional<PacienteDetalle> findByDni(String dni);

 boolean existsByDni(String dni);

 @Query("SELECT p FROM PacienteDetalle p JOIN FETCH p.usuario WHERE p.idUsuario = :idUsuario")
 Optional<PacienteDetalle> findByIdUsuarioWithUsuario(Integer idUsuario);

 @Query("SELECT p FROM PacienteDetalle p JOIN FETCH p.usuario u WHERE u.estadoRegistro = true")
 java.util.List<PacienteDetalle> findAllActivePatients();
}