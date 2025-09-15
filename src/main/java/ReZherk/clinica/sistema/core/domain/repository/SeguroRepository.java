package ReZherk.clinica.sistema.core.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ReZherk.clinica.sistema.core.domain.entity.Seguro;

@Repository
public interface SeguroRepository extends JpaRepository<Seguro, Integer> {
}