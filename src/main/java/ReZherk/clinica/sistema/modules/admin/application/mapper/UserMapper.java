package ReZherk.clinica.sistema.modules.admin.application.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.UserResponseDto;

@Component
public class UserMapper {

  public static UserResponseDto toDTO(Usuario usuario, String rol) {
    // Si rol es null, obtener todos los roles del usuario concatenados
    String rolFinal = rol;
    if (rolFinal == null || rolFinal.isEmpty()) {
      rolFinal = usuario.getPerfiles() != null && !usuario.getPerfiles().isEmpty()
          ? usuario.getPerfiles().stream()
              .map(r -> r.getNombre())
              .collect(Collectors.joining(", "))
          : "SIN ROL";
    }

    return UserResponseDto.builder()
        .id(usuario.getId())
        .numeroDocumento(usuario.getNumeroDocumento())
        .nombreCompleto(usuario.getNombres() + " " + usuario.getApellidos())
        .tipoDocumento(
            usuario.getTipoDocumento() != null
                ? usuario.getTipoDocumento().getNombre()
                : "SIN TIPO")
        .email(usuario.getEmail())
        .telefono(usuario.getTelefono())
        .rol(rolFinal)
        .estadoRegistro(usuario.getEstadoRegistro())
        .build();
  }
}