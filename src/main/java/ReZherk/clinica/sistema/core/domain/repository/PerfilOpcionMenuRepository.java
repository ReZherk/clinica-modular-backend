package ReZherk.clinica.sistema.core.domain.repository;

import ReZherk.clinica.sistema.core.domain.entity.PerfilOpcionMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerfilOpcionMenuRepository extends JpaRepository<PerfilOpcionMenu, Integer> {
 List<PerfilOpcionMenu> findByRol_Id(Integer idPerfil);
}
