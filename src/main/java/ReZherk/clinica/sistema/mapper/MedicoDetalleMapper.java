package ReZherk.clinica.sistema.mapper;

import ReZherk.clinica.sistema.dto.MedicoDetalleDto;
import ReZherk.clinica.sistema.entity.MedicoDetalle;
import ReZherk.clinica.sistema.entity.Usuario;
import ReZherk.clinica.sistema.entity.Especialidad;
import org.springframework.stereotype.Component;

@Component
public class MedicoDetalleMapper {

 public MedicoDetalle toEntity(MedicoDetalleDto dto, Usuario usuario) {
  return MedicoDetalle.builder()
    .usuario(usuario)
    .cmp(dto.getCmp())
    .especialidad(Especialidad.builder()
      .id(dto.getIdEspecialidad())
      .build())
    .build();
 }

 public MedicoDetalleDto toDto(MedicoDetalle entity) {
  return MedicoDetalleDto.builder()
    .cmp(entity.getCmp())
    .idEspecialidad(entity.getEspecialidad().getId())
    .build();
 }
}