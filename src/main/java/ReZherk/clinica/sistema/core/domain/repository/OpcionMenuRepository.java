package ReZherk.clinica.sistema.core.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ReZherk.clinica.sistema.core.domain.entity.OpcionMenu;

@Repository
public interface OpcionMenuRepository extends JpaRepository<OpcionMenu, Integer> {
}
