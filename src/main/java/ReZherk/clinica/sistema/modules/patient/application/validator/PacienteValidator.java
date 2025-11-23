package ReZherk.clinica.sistema.modules.patient.application.validator;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import ReZherk.clinica.sistema.core.domain.entity.Especialidad;
import ReZherk.clinica.sistema.core.domain.repository.EspecialidadRepository;
import ReZherk.clinica.sistema.core.shared.exception.BusinessException;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;

@Component
@RequiredArgsConstructor
public class PacienteValidator {

 private final EspecialidadRepository especialidadRepository;

 public String validateEspecialidadExists(Integer idSpecialty) {
  Especialidad especialidad = especialidadRepository.findByIdAndEstadoRegistroTrue(idSpecialty)
    .orElseThrow(() -> new ResourceNotFoundException(
      "La especialidad con id " + idSpecialty + " no existe o está inactiva"));

  // Validar que esté activa
  if (!especialidad.getEstadoRegistro()) {
   throw new BusinessException("La especialidad '" + especialidad.getNombreEspecialidad() + "' está inactiva");
  }

  return especialidad.getNombreEspecialidad();
 }
}