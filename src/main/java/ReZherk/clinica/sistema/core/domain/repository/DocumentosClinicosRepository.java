package ReZherk.clinica.sistema.core.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ReZherk.clinica.sistema.core.domain.entity.DocumentosClinicos;

import java.util.List;

@Repository
public interface DocumentosClinicosRepository extends JpaRepository<DocumentosClinicos, Integer> {
 List<DocumentosClinicos> findByDetalle_Id(Integer idDetalle);
}
