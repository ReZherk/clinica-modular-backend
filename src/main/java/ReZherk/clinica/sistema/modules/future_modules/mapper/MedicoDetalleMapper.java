package ReZherk.clinica.sistema.modules.future_modules.mapper;

import ReZherk.clinica.sistema.core.domain.entity.Especialidad;
import ReZherk.clinica.sistema.core.domain.entity.MedicoDetalle;
import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.modules.future_modules.dto.MedicoDetalleDto;

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