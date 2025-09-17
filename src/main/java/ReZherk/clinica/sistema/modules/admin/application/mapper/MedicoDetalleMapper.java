package ReZherk.clinica.sistema.modules.admin.application.mapper;

import org.springframework.stereotype.Component;

import ReZherk.clinica.sistema.core.domain.entity.Especialidad;
import ReZherk.clinica.sistema.core.domain.entity.MedicoDetalle;
import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.core.domain.repository.EspecialidadRepository;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.MedicoDetalleDto;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MedicoDetalleMapper {
 private final EspecialidadRepository especialidadRepository;

 public MedicoDetalle toEntity(MedicoDetalleDto dto, Usuario usuario) {
  Especialidad especialidad = especialidadRepository.findById(dto.getIdEspecialidad())
    .orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada"));

  return MedicoDetalle.builder()
    .usuario(usuario)
    .cmp(dto.getCmp())
    .especialidad(especialidad)
    .build();
 }
}
