package ReZherk.clinica.sistema.modules.patient.application.mapper;

import ReZherk.clinica.sistema.core.domain.entity.PacienteDetalle;
import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.modules.patient.application.dto.request.PacienteDetalleDto;

import org.springframework.stereotype.Component;

@Component
public class PacienteDetalleMapper {

  public PacienteDetalle toEntity(PacienteDetalleDto dto, Usuario usuario) {
    PacienteDetalle.PacienteDetalleBuilder builder = PacienteDetalle.builder()
        .usuario(usuario)
        .dni(dto.getDni())
        .fechaNacimiento(dto.getFechaNacimiento())
        .direccion(dto.getDireccion());

    return builder.build();
  }

  public PacienteDetalleDto toDto(PacienteDetalle entity) {
    return PacienteDetalleDto.builder()
        .dni(entity.getDni())
        .fechaNacimiento(entity.getFechaNacimiento())
        .direccion(entity.getDireccion())
        .build();
  }
}
