package ReZherk.clinica.sistema.core.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ReZherk.clinica.sistema.core.domain.entity.Seguro;

import java.util.List;

@Repository
public interface SeguroRepository extends JpaRepository<Seguro, Integer> {

 @Query("SELECT s FROM Seguro s WHERE s.estadoRegistro = true AND s.cubreCostoTotal = true ORDER BY s.nombreSeguro")
 List<Seguro> findSegurosConConvenio();

 List<Seguro> findByEstadoRegistroTrue();
}
