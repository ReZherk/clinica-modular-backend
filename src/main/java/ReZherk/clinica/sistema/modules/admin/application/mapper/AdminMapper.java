package ReZherk.clinica.sistema.modules.admin.application.mapper;

import org.springframework.stereotype.Component;

import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.AdminResponseDto;

@Component
public class AdminMapper {
  public static AdminResponseDto toDTO(Usuario usuario) {
    return AdminResponseDto.builder()
        .id(usuario.getId())
        .numeroDocumento(usuario.getNumeroDocumento())
        .nombreCompleto(usuario.getNombres() + " " + usuario.getApellidos())
        .email(usuario.getEmail())
        .telefono(usuario.getTelefono())
        .rol("ADMINISTRADOR")
        .estadoRegistro(usuario.getEstadoRegistro())
        .build();
  }

}
