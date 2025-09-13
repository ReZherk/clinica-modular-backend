package ReZherk.clinica.sistema.repository;

import ReZherk.clinica.sistema.entity.RolPerfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolPerfilRepository extends JpaRepository<RolPerfil, Integer> {
 Optional<RolPerfil> findByNombreIgnoreCase(String nombre);
}
