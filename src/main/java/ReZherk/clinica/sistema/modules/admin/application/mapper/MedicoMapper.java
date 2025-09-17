package ReZherk.clinica.sistema.modules.admin.application.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import ReZherk.clinica.sistema.core.application.dto.UsuarioBaseDto;
import ReZherk.clinica.sistema.core.domain.entity.RolPerfil;
import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.MedicalCreationResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.RegisterResponseDto;
import ReZherk.clinica.sistema.modules.patient.application.dto.response.UsuarioResponseDto;

@Component
public class MedicoMapper {

 public Usuario toEntity(UsuarioBaseDto dto) {
  return Usuario.builder()
    .nombres(dto.getNombres())
    .apellidos(dto.getApellidos())
    .email(dto.getEmail())
    .telefono(dto.getTelefono())
    .estadoRegistro(true)
    .build();
 }

 public RegisterResponseDto toRegisterResponse(Usuario usuario, String message) {
  return RegisterResponseDto.builder()
    .id(usuario.getId())
    .message(message)
    .usuario(toResponseDto(usuario))
    .build();
 }

 public MedicalCreationResponseDto toResponseDto(Usuario usuario) {
  Set<String> rolesNames = usuario.getPerfiles().stream()
    .map(rolPerfil -> rolPerfil
      .getNombre())
    .collect(Collectors.toSet());

  return MedicalCreationResponseDto.builder()
    .id(usuario.getId())
    .nombres(usuario.getNombres())
    .apellidos(usuario.getApellidos())
    .email(usuario.getEmail())
    .telefono(usuario.getTelefono())
    .estadoRegistro(usuario.getEstadoRegistro())
    .roles(rolesNames)
    .build();
 }
}
