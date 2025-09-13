package ReZherk.clinica.sistema.repository;

import ReZherk.clinica.sistema.entity.TokenSesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenSesionRepository extends JpaRepository<TokenSesion, Integer> {
 Optional<TokenSesion> findByTokenAndActivoTrue(String token);

 List<TokenSesion> findByUsuario_Id(Integer idUsuario);
}
