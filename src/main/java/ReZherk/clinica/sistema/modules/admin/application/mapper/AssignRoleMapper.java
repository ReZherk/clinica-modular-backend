package ReZherk.clinica.sistema.modules.admin.application.mapper;

import org.springframework.stereotype.Component;

import ReZherk.clinica.sistema.core.application.dto.UsuarioBaseDto;
import ReZherk.clinica.sistema.core.domain.entity.TipoDocumento;
import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.core.domain.repository.TipoDocumentoRepository;
import ReZherk.clinica.sistema.core.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AssignRoleMapper {

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

}
