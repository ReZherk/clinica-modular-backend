package ReZherk.clinica.sistema.modules.patient.application.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.core.domain.repository.UsuarioRepository;
import ReZherk.clinica.sistema.core.shared.exception.BusinessException;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;

@Component
@RequiredArgsConstructor
public class FotoPerfilValidator {

 private final UsuarioRepository usuarioRepository;

 /**
  * Valida que el usuario exista y este activo
  */
 public Usuario validateUsuarioExiste(Integer idUsuario) {
  Usuario usuario = usuarioRepository.findById(idUsuario)
    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

  if (!usuario.getEstadoRegistro()) {
   throw new BusinessException("El usuario no esta activo");
  }

  return usuario;
 }

 /**
  * Valida el archivo de foto
  */
 public void validateFoto(MultipartFile foto) {
  if (foto == null || foto.isEmpty()) {
   throw new BusinessException("Debe seleccionar una foto");
  }

  // Validar content type
  String contentType = foto.getContentType();
  if (contentType == null || !contentType.startsWith("image/")) {
   throw new BusinessException("El archivo debe ser una imagen");
  }
 }
}
