package ReZherk.clinica.sistema.modules.admin.application.mapper;

import org.springframework.stereotype.Component;

import ReZherk.clinica.sistema.core.domain.entity.Horario;
import ReZherk.clinica.sistema.core.shared.enums.DiaSemana;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.HorarioRequestDto;
import ReZherk.clinica.sistema.modules.admin.application.dto.response.HorarioResponseDto;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class HorarioMapper {

 private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

 public Horario toEntity(HorarioRequestDto dto) {
  if (dto == null) {
   return null;
  }

  return Horario.builder()
    .diaSemana(DiaSemana.valueOf(dto.getDiaSemana().toUpperCase()))
    .horaInicio(LocalTime.parse(dto.getHoraInicio(), TIME_FORMATTER))
    .horaFin(LocalTime.parse(dto.getHoraFin(), TIME_FORMATTER))
    .build();
 }

 public HorarioResponseDto toResponseDto(Horario entity) {
  if (entity == null) {
   return null;
  }

  return HorarioResponseDto.builder()
    .idHorario(entity.getIdHorario())
    .diaSemana(entity.getDiaSemana().name())
    .horaInicio(entity.getHoraInicio().format(TIME_FORMATTER))
    .horaFin(entity.getHoraFin().format(TIME_FORMATTER))
    .build();
 }
}
