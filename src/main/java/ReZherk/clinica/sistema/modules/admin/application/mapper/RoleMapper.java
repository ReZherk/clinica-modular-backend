package ReZherk.clinica.sistema.modules.admin.application.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import ReZherk.clinica.sistema.core.domain.entity.RolPerfil;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.RoleRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.RoleResponseDto;

@Component
public class RoleMapper {

  // Convierte de DTO -> Entidad
  public RolPerfil toEntity(RoleRequestDto dto) {
    return RolPerfil.builder()
        .nombre(dto.getNombre())
        .descripcion(dto.getDescripcion())
        .estadoRegistro(true)
        .build();
  }

  // Convierte de Entidad -> DTO
  public RoleResponseDto toDto(RolPerfil rol) {
    return RoleResponseDto.builder()
        .success(true)
        .message("Rol " + rol.getNombre() + " se cargo exitosamente")
        .id(rol.getId())
        .nombre(rol.getNombre())
        .descripcion(rol.getDescripcion())
        .estadoRegistro(rol.getEstadoRegistro())
        .build();
  }

  // Convierte lista de entidades -> lista de DTOs
  public List<RoleResponseDto> toDtoList(List<RolPerfil> roles) {
    return roles.stream()
        .map(role -> this.toDto(role))
        .collect(Collectors.toList());
  }
}
