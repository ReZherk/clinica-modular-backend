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
    Especialidad especialidad = especialidadRepository.findById(idEspecialidad)
        .orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada con ID: " + idEspecialidad));

    // Validar que esté activa
    if (!especialidad.getEstadoRegistro()) {
      throw new BusinessException("La especialidad '" + especialidad.getNombreEspecialidad() + "' está inactiva");
    }
  }

  public void validateEspecialidadExists(String especialidad) {
    if (especialidad != null && !especialidad.isEmpty()) {
      Especialidad esp = especialidadRepository.findByNombreEspecialidadIgnoreCase(especialidad)
          .orElseThrow(() -> new ResourceNotFoundException("La especialidad '" + especialidad + "' no existe"));

      // Validar que esté activa
      if (!esp.getEstadoRegistro()) {
        throw new BusinessException("La especialidad '" + especialidad + "' está inactiva");
      }
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

    boolean tieneRolMedicoActivo = usuario.getPerfiles().stream()
        .anyMatch(rol -> "MEDICO".equalsIgnoreCase(rol.getNombre()) && rol.getEstadoRegistro());

    if (!tieneRolMedicoActivo) {
      throw new IllegalStateException("El usuario [id=" + id + "] no tiene rol MEDICO activo");
    }

    return usuario;
  }

  public MedicoDetalle validateDetalleDelMedico(Integer id) {
    MedicoDetalle detalle = medicoDetalleRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Detalle no encontrado para el médico con id: " + id));

    // Validar que la especialidad esté activa
    if (detalle.getEspecialidad() == null || !detalle.getEspecialidad().getEstadoRegistro()) {
      throw new BusinessException("El médico no tiene una especialidad activa asignada");
    }

    return detalle;
  }

  public Especialidad validateEspecialidadExistsReturn(Integer id) {
    Especialidad especialidad = especialidadRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada con id: " + id));

    // Validar que esté activa
    if (!especialidad.getEstadoRegistro()) {
      throw new BusinessException("La especialidad '" + especialidad.getNombreEspecialidad() + "' está inactiva");
    }

    return especialidad;
  }
}