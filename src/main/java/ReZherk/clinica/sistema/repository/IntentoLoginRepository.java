package ReZherk.clinica.sistema.repository;

import ReZherk.clinica.sistema.entity.IntentoLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IntentoLoginRepository extends JpaRepository<IntentoLogin, Integer> {
 List<IntentoLogin> findByUsuario_Id(Integer idUsuario);
}
