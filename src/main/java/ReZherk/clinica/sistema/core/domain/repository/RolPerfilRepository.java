package ReZherk.clinica.sistema.core.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ReZherk.clinica.sistema.core.domain.entity.RolPerfil;

import java.util.Optional;
import java.util.List;

@Repository
public interface RolPerfilRepository extends JpaRepository<RolPerfil, Integer> {

 Optional<RolPerfil> findByNombre(String nombre);

 boolean existsByNombre(String nombre);

 @Query("SELECT r FROM RolPerfil r WHERE r.estadoRegistro = true")
 List<RolPerfil> findAllActive();

 @Query("SELECT r FROM RolPerfil r WHERE r.estadoRegistro = true ORDER BY r.nombre")
 List<RolPerfil> findAllActiveOrderByName();

 Optional<RolPerfil> findByNombreIgnoreCase(String nombre);

 @Query("""
       SELECT DISTINCT r FROM RolPerfil r
       LEFT JOIN FETCH r.permisos
       WHERE r.estadoRegistro = :estado
       AND (
           :search IS NULL OR :search = '' OR
           LOWER(r.nombre) LIKE LOWER(CONCAT('%', :search, '%'))
       )
   """)
 Page<RolPerfil> findActiveRolesBySearch(
   @Param("estado") Boolean estado,
   @Param("search") String search,
   Pageable pageable);
}