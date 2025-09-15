package ReZherk.clinica.sistema.core.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ReZherk.clinica.sistema.core.domain.entity.IntentoLogin;

import java.util.List;

@Repository
public interface IntentoLoginRepository extends JpaRepository<IntentoLogin, Integer> {
 List<IntentoLogin> findByUsuario_Id(Integer idUsuario);
}
