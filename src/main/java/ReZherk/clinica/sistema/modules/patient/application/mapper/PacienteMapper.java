package ReZherk.clinica.sistema.modules.patient.application.mapper;

import ReZherk.clinica.sistema.core.domain.entity.PacienteDetalle;
import ReZherk.clinica.sistema.core.domain.entity.RolPerfil;
import ReZherk.clinica.sistema.core.domain.entity.TipoDocumento;
import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.core.domain.repository.TipoDocumentoRepository;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;
import ReZherk.clinica.sistema.modules.patient.application.dto.request.PatientDataRequestDto;
import ReZherk.clinica.sistema.modules.patient.application.dto.response.PatientDataResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PacienteMapper {

  private final TipoDocumentoRepository tipoDocumentoRepository;

  public Usuario toEntity(PatientDataRequestDto dto) {
    if (dto == null)
      return null;

    TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(dto.getTipoDocumentoId())
        .orElseThrow(() -> new ResourceNotFoundException("Tipo de documento no encontrada"));

    return Usuario.builder()
        .nombres(dto.getNombres())
        .apellidos(dto.getApellidos())
        .tipoDocumento(tipoDocumento)
        .numeroDocumento(dto.getNumeroDocumento())
        .email(dto.getEmail())
        .telefono(dto.getTelefono())
        .estadoRegistro(true)
        .build();
  }

  public PatientDataResponseDto toRegisterResponse(Usuario usuario) {

    Set<String> rolesNames = usuario.getPerfiles().stream()
        .map(RolPerfil::getNombre)
        .collect(Collectors.toSet());

    PacienteDetalle detalle = usuario.getPacienteDetalle();

    return PatientDataResponseDto.builder()
        .id(usuario.getId())
        .tipoDocumento(usuario.getTipoDocumento() != null ? usuario.getTipoDocumento().getNombre() : null)
        .dni(usuario.getNumeroDocumento())

        .nombres(usuario.getNombres())
        .apellidos(usuario.getApellidos())
        .fechaNacimiento(detalle != null ? detalle.getFechaNacimiento().toString() : null)

        .telefono(usuario.getTelefono())
        .email(usuario.getEmail())

        .departamento(detalle != null ? detalle.getDepartamento() : null)
        .provincia(detalle != null ? detalle.getProvincia() : null)
        .distrito(detalle != null ? detalle.getDistrito() : null)
        .direccion(detalle != null ? detalle.getDireccion() : null)

        .estadoRegistro(usuario.getEstadoRegistro())
        .roles(rolesNames)
        .build();
  }

}
