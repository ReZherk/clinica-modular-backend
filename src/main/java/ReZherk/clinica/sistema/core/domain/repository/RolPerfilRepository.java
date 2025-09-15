package ReZherk.clinica.sistema.core.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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
}