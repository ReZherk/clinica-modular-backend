package ReZherk.clinica.sistema.modules.admin.application.mapper;

import ReZherk.clinica.sistema.core.domain.entity.Permission;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.PermissionResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PermissionMapper {

 public PermissionResponseDto toDto(Permission entity) {
  if (entity == null)
   return null;

  return PermissionResponseDto.builder()
    .id(entity.getId())
    .name(entity.getName())
    .description(entity.getDescription())
    .actionKey(entity.getActionKey())
    .build();
 }

 public List<PermissionResponseDto> toDtoList(List<Permission> entities) {
  return entities.stream().map(this::toDto).collect(Collectors.toList());
 }
}
