package ReZherk.clinica.sistema.repository;

import ReZherk.clinica.sistema.entity.UsuarioPerfil;
import ReZherk.clinica.sistema.entity.UsuarioPerfilId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioPerfilRepository extends JpaRepository<UsuarioPerfil, UsuarioPerfilId> {
 List<UsuarioPerfil> findByUsuario_Id(Integer idUsuario);

 List<UsuarioPerfil> findByRolPerfil_Id(Integer idPerfil);
}