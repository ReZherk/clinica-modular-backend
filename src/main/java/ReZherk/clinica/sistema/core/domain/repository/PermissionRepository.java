package ReZherk.clinica.sistema.core.domain.repository;

import ReZherk.clinica.sistema.core.domain.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * REPOSITORIO PARA MANEJAR OPERACIONES DE PERMISOS EN LA BD
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

 // Buscar permiso por actionKey (ej: "USER_READ")
 Optional<Permission> findByActionKey(String actionKey);

 // Verificar si existe un permiso por actionKey
 Boolean existsByActionKey(String actionKey);
}