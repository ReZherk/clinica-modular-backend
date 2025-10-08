package ReZherk.clinica.sistema.core.domain.repository;

import ReZherk.clinica.sistema.core.domain.entity.Usuario;

import org.springdoc.core.converters.models.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

  // Buscar usuario por email
  Optional<Usuario> findByEmail(String email);

  // Verificar si existe email
  boolean existsByEmail(String email);

  // Buscar usuario con sus roles/perfiles
  @Query("SELECT u FROM Usuario u JOIN FETCH u.perfiles WHERE u.email = :email")
  Optional<Usuario> findByEmailWithRoles(@Param("email") String email);

  // Buscar usuario con roles + permisos
  @Query("""
          SELECT DISTINCT u
          FROM Usuario u
          LEFT JOIN FETCH u.perfiles r
          LEFT JOIN FETCH r.permisos p
          WHERE u.email = :email
      """)
  Optional<Usuario> findByEmailWithRolesAndPermissions(@Param("email") String email);

  // Listar usuarios activos
  @Query("SELECT u FROM Usuario u WHERE u.estadoRegistro = true")
  List<Usuario> findAllActive();

  // Buscar por DNI
  Optional<Usuario> findByDni(String dni);

  // Verificar si existe DNI
  boolean existsByDni(String dni);

  @Query("""
          SELECT DISTINCT u FROM Usuario u
          WHERE u.estadoRegistro = :estado
          AND EXISTS (
              SELECT 1 FROM RolPerfil r
              WHERE r MEMBER OF u.perfiles
              AND UPPER(r.nombre) = UPPER(:rol)
          )
          AND (
              :search IS NULL OR :search = '' OR
              LOWER(CONCAT(u.nombres, ' ', u.apellidos)) LIKE LOWER(CONCAT('%', :search, '%')) OR
              u.dni LIKE CONCAT('%', :search, '%')
          )
      """)
  Page<Usuario> findByEstadoAndRolAndSearch(
      @Param("estado") Boolean estado,
      @Param("rol") String rol,
      @Param("search") String search,
      Pageable pageable);
}
