package ReZherk.clinica.sistema.modules.admin.application.validator;

import org.springframework.stereotype.Component;

import ReZherk.clinica.sistema.core.domain.entity.Especialidad;
import ReZherk.clinica.sistema.core.domain.entity.MedicoDetalle;
import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.core.domain.repository.EspecialidadRepository;
import ReZherk.clinica.sistema.core.domain.repository.MedicoDetalleRepository;
import ReZherk.clinica.sistema.core.domain.repository.UsuarioRepository;
import ReZherk.clinica.sistema.core.shared.exception.BusinessException;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MedicoValidator {

 private final MedicoDetalleRepository medicoDetalleRepository;
 private final EspecialidadRepository especialidadRepository;
 private final CommonValidator commonValidator;
 private final UsuarioRepository usuarioRepository;

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

 public Usuario validateUsuarioEsMedico(Integer id) {
  Usuario usuario = usuarioRepository.findById(id)
    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado [id=" + id + "]"));

  boolean tieneRolMedico = usuario.getPerfiles().stream()
    .anyMatch(rol -> "MEDICO".equalsIgnoreCase(rol.getNombre()));

  if (!tieneRolMedico) {
   throw new IllegalStateException("El usuario [id=" + id + "] no tiene rol MEDICO");
  }

  return usuario;
 }

 public MedicoDetalle validateDetalleDelMedico(Integer id) {

  MedicoDetalle detalle = medicoDetalleRepository.findById(id)
    .orElseThrow(() -> new RuntimeException("Detalle no encontrado para el médico con id: " + id));

  return detalle;
 }

 public Especialidad validateEspecialidadExistsReturn(Integer id) {

  Especialidad especialidad = especialidadRepository.findById(id)
    .orElseThrow(() -> new RuntimeException(
      "Especialidad no encontrada con id: " + id));

  return especialidad;
 }

}
