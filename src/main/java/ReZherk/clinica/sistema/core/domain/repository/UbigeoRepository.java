package ReZherk.clinica.sistema.core.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ReZherk.clinica.sistema.core.domain.entity.Ubigeo;

@Repository
public interface UbigeoRepository extends JpaRepository<Ubigeo, Integer> {
}