package ReZherk.clinica.sistema.core.domain.repository;

import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

  Optional<Usuario> findByEmail(String email);

  // Verificar si existe email
  boolean existsByEmail(String email);

  // Traer usuario con roles por email
  @Query("SELECT u FROM Usuario u JOIN FETCH u.perfiles WHERE u.email = :email")
  Optional<Usuario> findByEmailWithRoles(@Param("email") String email);

  // Listar usuarios activos
  @Query("SELECT u FROM Usuario u WHERE u.estadoRegistro = true")
  List<Usuario> findAllActive();

  Optional<Usuario> findByDni(String dni);

  boolean existsByDni(String dni);
}
