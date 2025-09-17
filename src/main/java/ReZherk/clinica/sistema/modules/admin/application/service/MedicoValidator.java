package ReZherk.clinica.sistema.modules.admin.application.service;

import org.springframework.stereotype.Component;

import ReZherk.clinica.sistema.core.domain.repository.EspecialidadRepository;
import ReZherk.clinica.sistema.core.domain.repository.MedicoDetalleRepository;
import ReZherk.clinica.sistema.core.domain.repository.UsuarioRepository;
import ReZherk.clinica.sistema.core.shared.exception.BusinessException;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MedicoValidator {

 private final UsuarioRepository usuarioRepository;
 private final MedicoDetalleRepository medicoDetalleRepository;
 private final EspecialidadRepository especialidadRepository;

 // ===== VALIDACIONES =====
 public void validateEmailNotExists(String email) {
  if (usuarioRepository.existsByEmail(email)) {
   throw new BusinessException("Ya existe un usuario con el email: " + email);
  }
 }

 public void validateDniNotExists(String dni) {
  if (dni != null && usuarioRepository.existsByDni(dni)) {
   throw new BusinessException("Ya existe un paciente con el DNI: " + dni);
  }
 }

 public void validateCmpNotExists(String cmp) {
  if (medicoDetalleRepository.existsByCmp(cmp)) {
   throw new BusinessException("Ya existe un médico con el CMP: " + cmp);
  }
 }

 public void validateEspecialidadExists(Integer idEspecialidad) {
  if (!especialidadRepository.existsById(idEspecialidad)) {
   throw new ResourceNotFoundException("Especialidad no encontrada con ID: " + idEspecialidad);
  }
 }

}
