package ReZherk.clinica.sistema.core.domain.repository;

import ReZherk.clinica.sistema.core.domain.entity.UsuarioPerfil;
import ReZherk.clinica.sistema.core.domain.entity.UsuarioPerfilId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UsuarioPerfilRepository extends JpaRepository<UsuarioPerfil, UsuarioPerfilId> {
 List<UsuarioPerfil> findByIdUsuario(Integer idUsuario);
}
