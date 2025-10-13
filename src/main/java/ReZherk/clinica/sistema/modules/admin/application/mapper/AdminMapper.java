package ReZherk.clinica.sistema.modules.admin.application.mapper;

import org.springframework.stereotype.Component;

import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.UserResponseDto;

@Component
public class AdminMapper {
  public static UserResponseDto toDTO(Usuario usuario, String rol) {
    return UserResponseDto.builder()
        .id(usuario.getId())
        .numeroDocumento(usuario.getNumeroDocumento())
        .nombreCompleto(usuario.getNombres() + " " + usuario.getApellidos())
        .tipoDocumento(usuario.getTipoDocumento().getNombre())
        .email(usuario.getEmail())
        .telefono(usuario.getTelefono())
        .rol(rol)
        .estadoRegistro(usuario.getEstadoRegistro())
        .build();
  }

}
