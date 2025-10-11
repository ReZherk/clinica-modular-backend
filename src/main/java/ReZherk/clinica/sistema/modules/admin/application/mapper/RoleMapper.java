package ReZherk.clinica.sistema.modules.admin.application.mapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import ReZherk.clinica.sistema.core.domain.entity.Permission;
import ReZherk.clinica.sistema.core.domain.entity.RolPerfil;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.RoleRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.PermissionDto;
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
    if (rol == null) {
      return null;
    }

    return RoleResponseDto.builder()
        .id(rol.getId())
        .nombre(rol.getNombre())
        .descripcion(rol.getDescripcion())
        .estadoRegistro(rol.getEstadoRegistro())
        .permisos(mapPermisos(rol.getPermisos()))
        .build();
  }

  private static Set<PermissionDto> mapPermisos(Set<Permission> permisos) {
    if (permisos == null) {
      return Set.of();
    }

    return permisos.stream()
        .map(permiso -> toPermissionDO(permiso))
        .collect(Collectors.toSet());
  }

  private static PermissionDto toPermissionDO(Permission permission) {
    if (permission == null) {
      return null;
    }

    return PermissionDto.builder()
        .id(permission.getId())
        .name(permission.getName())
        .description(permission.getDescription())
        .actionKey(permission.getActionKey())
        .build();
  }

  // Convierte lista de entidades -> lista de DTOs
  public List<RoleResponseDto> toDtoList(List<RolPerfil> roles) {
    return roles.stream()
        .map(role -> this.toDto(role))
        .collect(Collectors.toList());
  }
}
