package ReZherk.clinica.sistema.core.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ReZherk.clinica.sistema.core.domain.entity.MedicoDetalle;

import java.util.Optional;
import java.util.List;

@Repository
public interface MedicoDetalleRepository extends JpaRepository<MedicoDetalle, Integer> {

  Optional<MedicoDetalle> findByCmp(String cmp);

  boolean existsByCmp(String cmp);

  @Query("SELECT m FROM MedicoDetalle m JOIN FETCH m.usuario WHERE m.idUsuario = :idUsuario")
  Optional<MedicoDetalle> findByIdUsuarioWithUsuario(Integer idUsuario);

  @Query("SELECT m FROM MedicoDetalle m JOIN FETCH m.usuario u WHERE u.estadoRegistro = true")
  List<MedicoDetalle> findAllActiveDoctors();

  @Query("SELECT m FROM MedicoDetalle m JOIN FETCH m.especialidad WHERE m.especialidad.id = :idEspecialidad")
  List<MedicoDetalle> findByEspecialidadId(Integer idEspecialidad);
}