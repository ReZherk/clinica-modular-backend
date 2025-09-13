package ReZherk.clinica.sistema.repository;

import ReZherk.clinica.sistema.entity.PerfilOpcionMenu;
import ReZherk.clinica.sistema.entity.PerfilOpcionMenuId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerfilOpcionMenuRepository extends JpaRepository<PerfilOpcionMenu, PerfilOpcionMenuId> {
 List<PerfilOpcionMenu> findByIdPerfil(Integer idPerfil);
}
