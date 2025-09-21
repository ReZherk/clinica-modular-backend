package ReZherk.clinica.sistema.modules.admin.application.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import ReZherk.clinica.sistema.core.application.dto.UsuarioBaseDto;
import ReZherk.clinica.sistema.core.domain.entity.TipoDocumento;
import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.core.domain.repository.TipoDocumentoRepository;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.MedicalCreationResponseDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.RegisterResponseDto;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MedicoMapper {

  private final TipoDocumentoRepository tipoDocumentoRepository;

  public Usuario toEntity(UsuarioBaseDto dto) {
    TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(dto.getTipoDocumentoId())
        .orElseThrow(() -> new ResourceNotFoundException("Tipo de documento no encontrada"));

    return Usuario.builder()
        .nombres(dto.getNombres())
        .apellidos(dto.getApellidos())
        .dni(dto.getDni())
        .fechaEmision(dto.getFechaEmision())
        .tipoDocumento(tipoDocumento)
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
        .dni(usuario.getDni())
        .email(usuario.getEmail())
        .telefono(usuario.getTelefono())
        .estadoRegistro(usuario.getEstadoRegistro())
        .roles(rolesNames)
        .build();
  }
}
