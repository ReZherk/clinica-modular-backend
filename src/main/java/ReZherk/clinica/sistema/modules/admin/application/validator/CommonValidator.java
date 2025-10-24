package ReZherk.clinica.sistema.modules.admin.application.validator;

import org.springframework.stereotype.Component;

import ReZherk.clinica.sistema.core.domain.repository.UsuarioRepository;
import ReZherk.clinica.sistema.core.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CommonValidator {

 private final UsuarioRepository usuarioRepository;

 public void validateEmailNotExists(String email) {
  if (usuarioRepository.existsByEmail(email)) {
   throw new BusinessException("Ya existe un usuario con el email: " + email);
  }
 }

 public void validateDocumentoNotExists(String numeroDocumento) {
  if (numeroDocumento != null && usuarioRepository.existsByNumeroDocumento(numeroDocumento)) {
   throw new BusinessException("Ya existe un usuario con el documento: " + numeroDocumento);
  }
 }

 public void validateUsuarioUniqueFields(String email, String numeroDocumento) {
  validateEmailNotExists(email);
  validateDocumentoNotExists(numeroDocumento);
 }

}
