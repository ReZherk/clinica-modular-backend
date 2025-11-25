package ReZherk.clinica.sistema.modules.medico.application.mapper;

import org.springframework.stereotype.Component;

import ReZherk.clinica.sistema.core.domain.entity.Cita;
import ReZherk.clinica.sistema.core.domain.entity.DetalleCita;
import ReZherk.clinica.sistema.modules.medico.application.dto.request.DetalleCitaRequestDto;
import ReZherk.clinica.sistema.modules.medico.application.dto.response.DetalleCitaResponseDto;

@Component
public class DetalleCitaMapper {

 public DetalleCita toEntity(DetalleCitaRequestDto dto, Cita cita) {
  return DetalleCita.builder()
    .cita(cita)
    .diagnostico(dto.getDiagnostico())
    .receta(dto.getReceta())
    .observaciones(dto.getObservaciones())
    .build();
 }

 public DetalleCitaResponseDto toResponseDto(DetalleCita detalleCita) {
  if (detalleCita == null)
   return null;

  return DetalleCitaResponseDto.builder()
    .idDetalleCita(detalleCita.getIdDetalleCita())
    .idCita(detalleCita.getCita().getIdCita())
    .diagnostico(detalleCita.getDiagnostico())
    .receta(detalleCita.getReceta())
    .observaciones(detalleCita.getObservaciones())
    .fechaRegistro(detalleCita.getFechaRegistro())
    .fechaActualizacion(detalleCita.getFechaActualizacion())
    .build();
 }

 public void updateEntity(DetalleCita detalleCita, DetalleCitaRequestDto dto) {
  detalleCita.setDiagnostico(dto.getDiagnostico());
  detalleCita.setReceta(dto.getReceta());
  detalleCita.setObservaciones(dto.getObservaciones());
 }
}
