package ReZherk.clinica.sistema.core.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ReZherk.clinica.sistema.core.domain.entity.Usuario;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

 Optional<Usuario> findByEmail(String email);

 boolean existsByEmail(String email);

 @Query("SELECT u FROM Usuario u JOIN FETCH u.perfiles WHERE u.email = :email")
 Optional<Usuario> findByEmailWithRoles(@Param("email") String email);

 @Query("SELECT u FROM Usuario u WHERE u.estadoRegistro = true")
 java.util.List<Usuario> findAllActive();

 @Query("""
      SELECT u
      FROM Usuario u
      JOIN PacienteDetalle p ON p.usuario.id = u.id
      WHERE p.dni = :dni
   """)
 Optional<Usuario> findByDni(@Param("dni") String dni);

}
