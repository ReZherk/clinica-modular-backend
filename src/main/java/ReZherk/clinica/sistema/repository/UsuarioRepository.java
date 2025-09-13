package ReZherk.clinica.sistema.repository;

import ReZherk.clinica.sistema.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
 Optional<Usuario> findByEmail(String email);

 List<Usuario> findByEstadoRegistroTrue();
}
