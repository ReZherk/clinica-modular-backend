package ReZherk.clinica.sistema.repository;

import ReZherk.clinica.sistema.entity.UsuarioPerfil;
import ReZherk.clinica.sistema.entity.UsuarioPerfilId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioPerfilRepository extends JpaRepository<UsuarioPerfil, UsuarioPerfilId> {

 List<UsuarioPerfil> findByIdUsuario(Integer idUsuario);

 List<UsuarioPerfil> findByIdPerfil(Integer idPerfil);

 @Query("SELECT up FROM UsuarioPerfil up JOIN FETCH up.rolPerfil WHERE up.idUsuario = :idUsuario")
 List<UsuarioPerfil> findByIdUsuarioWithRoles(@Param("idUsuario") Integer idUsuario);

 boolean existsByIdUsuarioAndIdPerfil(Integer idUsuario, Integer idPerfil);

 void deleteByIdUsuarioAndIdPerfil(Integer idUsuario, Integer idPerfil);
}