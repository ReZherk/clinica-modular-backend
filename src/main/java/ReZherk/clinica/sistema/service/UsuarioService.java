package ReZherk.clinica.sistema.service;

import ReZherk.clinica.sistema.dto.UsuarioRequestDTO;
import ReZherk.clinica.sistema.dto.UsuarioResponseDTO;

import java.util.List;

public interface UsuarioService {
 UsuarioResponseDTO registrarUsuario(UsuarioRequestDTO dto);

 UsuarioResponseDTO buscarPorEmail(String email);

 List<UsuarioResponseDTO> listarUsuarios();
}
