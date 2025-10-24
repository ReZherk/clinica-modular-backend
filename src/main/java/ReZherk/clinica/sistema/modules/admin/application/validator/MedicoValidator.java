package ReZherk.clinica.sistema.modules.admin.application.validator;

import org.springframework.stereotype.Component;

import ReZherk.clinica.sistema.core.domain.repository.EspecialidadRepository;
import ReZherk.clinica.sistema.core.domain.repository.MedicoDetalleRepository;
import ReZherk.clinica.sistema.core.shared.exception.BusinessException;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MedicoValidator {

 private final MedicoDetalleRepository medicoDetalleRepository;
 private final EspecialidadRepository especialidadRepository;
 private final CommonValidator commonValidator;

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

 public void validateForCreation(String email, String numeroDocumento, String cmp, Integer idEspecialidad) {
  commonValidator.validateUsuarioUniqueFields(email, numeroDocumento);

  validateCmpNotExists(cmp);
  validateEspecialidadExists(idEspecialidad);
 }

}
