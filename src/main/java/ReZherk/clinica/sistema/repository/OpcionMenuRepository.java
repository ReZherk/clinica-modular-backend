package ReZherk.clinica.sistema.repository;

import ReZherk.clinica.sistema.entity.OpcionMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpcionMenuRepository extends JpaRepository<OpcionMenu, Integer> {
}
