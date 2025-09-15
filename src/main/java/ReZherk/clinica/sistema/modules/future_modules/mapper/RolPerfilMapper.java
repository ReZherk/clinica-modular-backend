package ReZherk.clinica.sistema.modules.future_modules.mapper;

import ReZherk.clinica.sistema.core.domain.entity.RolPerfil;
import ReZherk.clinica.sistema.modules.patient.application.dto.request.CreateRolPerfilDto;
import ReZherk.clinica.sistema.modules.patient.application.dto.response.RolPerfilDto;

import org.springframework.stereotype.Component;

@Component
public class RolPerfilMapper {

  public RolPerfil toEntity(CreateRolPerfilDto dto) {
    return RolPerfil.builder()
        .nombre(dto.getNombre())
        .descripcion(dto.getDescripcion())
        .estadoRegistro(true)
        .build();
  }

  public RolPerfilDto toDto(RolPerfil entity) {
    return RolPerfilDto.builder()
        .id(entity.getId())
        .nombre(entity.getNombre())
        .descripcion(entity.getDescripcion())
        .estadoRegistro(entity.getEstadoRegistro())
        .build();
  }

  public void updateEntityFromDto(RolPerfil entity, CreateRolPerfilDto dto) {
    entity.setNombre(dto.getNombre());
    entity.setDescripcion(dto.getDescripcion());
  }
}
