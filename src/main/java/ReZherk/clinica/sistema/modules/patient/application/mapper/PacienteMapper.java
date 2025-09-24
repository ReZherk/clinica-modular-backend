package ReZherk.clinica.sistema.modules.patient.application.mapper;

import ReZherk.clinica.sistema.core.domain.entity.TipoDocumento;
import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.core.domain.repository.TipoDocumentoRepository;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;
import ReZherk.clinica.sistema.modules.patient.application.dto.request.RegisterPacienteDto;
import ReZherk.clinica.sistema.modules.patient.application.dto.response.RegisterResponseDto;
import lombok.RequiredArgsConstructor;
import ReZherk.clinica.sistema.modules.patient.application.dto.response.PatientCreationResponseDto;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PacienteMapper {

  private final TipoDocumentoRepository tipoDocumentoRepository;

  public Usuario toEntity(RegisterPacienteDto dto) {
    if (dto == null)
      return null;

    TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(dto.getTipoDocumentoId())
        .orElseThrow(() -> new ResourceNotFoundException("Tipo de documento no encontrada"));

    return Usuario.builder()
        .nombres(dto.getNombres())
        .apellidos(dto.getApellidos())
        .tipoDocumento(tipoDocumento)
        .dni(dto.getDni())
        .fechaEmision(dto.getFechaEmision())
        .email(dto.getEmail())
        .telefono(dto.getTelefono())
        .estadoRegistro(true)
        .build();
  }

  public RegisterResponseDto toRegisterResponse(Usuario usuario, String message) {
    return RegisterResponseDto.builder()
        .success(true)
        .id(usuario.getId())
        .message(message)
        .usuario(
            toResponseDto(usuario))
        .build();
  }

  public PatientCreationResponseDto toResponseDto(Usuario usuario) {
    Set<String> rolesNames = usuario.getPerfiles().stream()
        .map(rolPerfil -> rolPerfil
            .getNombre())
        .collect(Collectors.toSet());

    return PatientCreationResponseDto.builder()
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
