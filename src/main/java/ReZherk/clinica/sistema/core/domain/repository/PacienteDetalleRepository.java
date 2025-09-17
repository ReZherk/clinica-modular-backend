package ReZherk.clinica.sistema.core.domain.repository;

import ReZherk.clinica.sistema.core.domain.entity.PacienteDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteDetalleRepository extends JpaRepository<PacienteDetalle, Integer> {

 @Query("SELECT p FROM PacienteDetalle p JOIN FETCH p.usuario WHERE p.idUsuario = :idUsuario")
 Optional<PacienteDetalle> findByIdUsuarioWithUsuario(Integer idUsuario);

 @Query("""
    SELECT p
    FROM PacienteDetalle p
    JOIN FETCH p.usuario u
    WHERE u.estadoRegistro = true
   """)
 List<PacienteDetalle> findAllActivePatients();
}
