package ReZherk.clinica.sistema.modules.patient.application.mapper;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import ReZherk.clinica.sistema.core.domain.entity.MedicoDetalle;
import ReZherk.clinica.sistema.core.domain.entity.Usuario;
import ReZherk.clinica.sistema.modules.patient.application.dto.response.DoctorBySpecialtyResponseDto;

@Component
@RequiredArgsConstructor
public class DoctorBySpecialtyMapper {

 public static DoctorBySpecialtyResponseDto toDto(Usuario usuario, MedicoDetalle detalle) {
  return DoctorBySpecialtyResponseDto.builder()
    .id(usuario.getId())
    .nombresCompleto(usuario.getNombres() + " " + usuario.getApellidos())
    .estadoRegistro(usuario.getEstadoRegistro())
    .cmp(detalle != null ? detalle.getCmp() : null)
    .especialidad(detalle != null && detalle.getEspecialidad() != null
      ? detalle.getEspecialidad().getNombreEspecialidad()
      : null)
    .build();
 }

}
