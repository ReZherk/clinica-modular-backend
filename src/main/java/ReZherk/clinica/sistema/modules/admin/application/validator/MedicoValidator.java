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

 public void validateEspecialidadExistsId(Integer idEspecialidad) {
  if (!especialidadRepository.existsById(idEspecialidad)) {
   throw new ResourceNotFoundException("Especialidad no encontrada con ID: " + idEspecialidad);
  }
 }

 public void validateEspecialidadExists(String especialidad) {
  if (especialidad != null && !especialidad.isEmpty()
    && !especialidadRepository.existsByNombreEspecialidad(especialidad)) {

   throw new RuntimeException("La especialidad '" + especialidad + "' no existe");
  }
 }

 public void validateForCreation(String email, String numeroDocumento, String cmp, Integer idEspecialidad) {
  commonValidator.validateUsuarioUniqueFields(email, numeroDocumento);

  validateCmpNotExists(cmp);
  validateEspecialidadExistsId(idEspecialidad);
 }

}
