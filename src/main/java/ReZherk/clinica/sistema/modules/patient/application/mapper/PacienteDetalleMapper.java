package ReZherk.clinica.sistema.modules.patient.application.mapper;

import ReZherk.clinica.sistema.core.domain.entity.PacienteDetalle;
import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.modules.patient.application.dto.request.PacienteDetalleDto;
import org.springframework.stereotype.Component;

@Component
public class PacienteDetalleMapper {

  public PacienteDetalle toEntity(PacienteDetalleDto dto, Usuario usuario) {
    return PacienteDetalle.builder()
        .usuario(usuario)
        .fechaNacimiento(dto.getFechaNacimiento())
        .direccion(dto.getDireccion())
        .departamento(dto.getDepartamento())
        .provincia(dto.getProvincia())
        .distrito(dto.getDistrito())
        .build();
  }

  public PacienteDetalleDto toDto(PacienteDetalle entity) {
    return PacienteDetalleDto.builder()
        .fechaNacimiento(entity.getFechaNacimiento())
        .direccion(entity.getDireccion())
        .departamento(entity.getDepartamento())
        .provincia(entity.getProvincia())
        .distrito(entity.getDistrito())
        .build();
  }
}
