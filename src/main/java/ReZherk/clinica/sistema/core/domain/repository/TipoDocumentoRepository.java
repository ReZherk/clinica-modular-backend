package ReZherk.clinica.sistema.core.domain.repository;

import ReZherk.clinica.sistema.core.domain.entity.TipoDocumento;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoDocumentoRepository extends JpaRepository<TipoDocumento, Integer> {

 // Buscar por nombre exacto
 Optional<TipoDocumento> findByNombre(String nombre);

 // Listar todos los activos
 List<TipoDocumento> findByEstadoRegistroTrue();
}
