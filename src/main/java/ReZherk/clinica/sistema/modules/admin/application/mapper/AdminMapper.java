package ReZherk.clinica.sistema.modules.admin.application.mapper;

import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.AdminResponseDto;

public class AdminMapper {
 public static AdminResponseDto toDTO(Usuario usuario) {
  return AdminResponseDto.builder()
    .id(usuario.getId())
    .dni(usuario.getDni())
    .nombreCompleto(usuario.getNombres() + " " + usuario.getApellidos())
    .rol("ADMINISTRADOR")
    .estadoRegistro(usuario.getEstadoRegistro())
    .build();
 }
}
