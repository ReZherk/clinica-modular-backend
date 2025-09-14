package ReZherk.clinica.sistema.mapper;

import ReZherk.clinica.sistema.dto.PacienteDetalleDto;
import ReZherk.clinica.sistema.entity.PacienteDetalle;
import ReZherk.clinica.sistema.entity.Usuario;
import ReZherk.clinica.sistema.entity.Seguro;
import ReZherk.clinica.sistema.entity.Ubigeo;
import org.springframework.stereotype.Component;

@Component
public class PacienteDetalleMapper {

 public PacienteDetalle toEntity(PacienteDetalleDto dto, Usuario usuario) {
  PacienteDetalle.PacienteDetalleBuilder builder = PacienteDetalle.builder()
    .usuario(usuario)
    .dni(dto.getDni())
    .fechaNacimiento(dto.getFechaNacimiento())
    .direccion(dto.getDireccion());

  // Relaciones opcionales - se asignarán en el servicio si es necesario
  if (dto.getIdSeguro() != null) {
   builder.seguro(Seguro.builder().id(dto.getIdSeguro()).build());
  }

  if (dto.getIdUbigeo() != null) {
   builder.ubigeo(Ubigeo.builder().id(dto.getIdUbigeo()).build());
  }

  return builder.build();
 }

 public PacienteDetalleDto toDto(PacienteDetalle entity) {
  return PacienteDetalleDto.builder()
    .dni(entity.getDni())
    .fechaNacimiento(entity.getFechaNacimiento())
    .direccion(entity.getDireccion())
    .idSeguro(entity.getSeguro() != null ? entity.getSeguro().getId() : null)
    .idUbigeo(entity.getUbigeo() != null ? entity.getUbigeo().getId() : null)
    .build();
 }
}
