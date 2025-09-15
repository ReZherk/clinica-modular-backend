package ReZherk.clinica.sistema.core.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ReZherk.clinica.sistema.core.domain.entity.PerfilOpcionMenu;
import ReZherk.clinica.sistema.core.domain.entity.PerfilOpcionMenuId;

import java.util.List;

@Repository
public interface PerfilOpcionMenuRepository extends JpaRepository<PerfilOpcionMenu, PerfilOpcionMenuId> {
 List<PerfilOpcionMenu> findByIdPerfil(Integer idPerfil);
}
