package ReZherk.clinica.sistema.modules.admin.application.validator;

import org.springframework.stereotype.Component;

import ReZherk.clinica.sistema.modules.admin.application.dto.request.HorarioRequestDto;
import jakarta.validation.ValidationException;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class HorarioValidator {

 private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
 private static final List<String> DIAS_VALIDOS = Arrays.asList(
   "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado");
 private static final LocalTime HORA_APERTURA = LocalTime.of(7, 0);
 private static final LocalTime HORA_CIERRE = LocalTime.of(20, 0);

 public void validarHorario(HorarioRequestDto dto) {
  List<String> errores = new ArrayList<>();

  // Validar día de la semana
  if (!DIAS_VALIDOS.contains(dto.getDiaSemana())) {
   errores.add("Día inválido. Debe ser uno de: " + String.join(", ", DIAS_VALIDOS));
  }

  LocalTime horaInicio;
  LocalTime horaFin;

  try {
   horaInicio = LocalTime.parse(dto.getHoraInicio(), TIME_FORMATTER);
  } catch (DateTimeParseException e) {
   errores.add("Formato de hora de inicio inválido. Use HH:mm");
   throw new ValidationException(String.join("; ", errores));
  }

  try {
   horaFin = LocalTime.parse(dto.getHoraFin(), TIME_FORMATTER);
  } catch (DateTimeParseException e) {
   errores.add("Formato de hora de fin inválido. Use HH:mm");
   throw new ValidationException(String.join("; ", errores));
  }

  // Validar que hora inicio sea menor que hora fin
  if (!horaInicio.isBefore(horaFin)) {
   errores.add("La hora de inicio debe ser anterior a la hora de fin");
  }

  // Validar horario de atención
  if (horaInicio.isBefore(HORA_APERTURA)) {
   errores.add("La hora de inicio no puede ser antes de las 07:00");
  }

  if (horaFin.isAfter(HORA_CIERRE)) {
   errores.add("La hora de fin no puede ser después de las 20:00");
  }

  // Validar que el bloque sea múltiplo de 30 minutos
  long minutos = java.time.Duration.between(horaInicio, horaFin).toMinutes();
  if (minutos % 30 != 0) {
   errores.add("El bloque horario debe ser múltiplo de 30 minutos");
  }

  if (minutos < 30) {
   errores.add("El bloque horario debe ser de al menos 30 minutos");
  }

  if (!errores.isEmpty()) {
   throw new ValidationException(String.join("; ", errores));
  }
 }

 public void validarHorarios(List<HorarioRequestDto> horarios) {
  if (horarios == null || horarios.isEmpty()) {
   throw new ValidationException("Debe proporcionar al menos un horario");
  }

  // Validar cada horario individualmente
  for (HorarioRequestDto horario : horarios) {
   validarHorario(horario);
  }

  // Validar que no haya solapamientos en el mismo día
  validarSolapamientos(horarios);
 }

 private void validarSolapamientos(List<HorarioRequestDto> horarios) {
  for (int i = 0; i < horarios.size(); i++) {
   HorarioRequestDto h1 = horarios.get(i);
   LocalTime inicio1 = LocalTime.parse(h1.getHoraInicio(), TIME_FORMATTER);
   LocalTime fin1 = LocalTime.parse(h1.getHoraFin(), TIME_FORMATTER);

   for (int j = i + 1; j < horarios.size(); j++) {
    HorarioRequestDto h2 = horarios.get(j);

    // Solo validar solapamientos del mismo día
    if (h1.getDiaSemana().equals(h2.getDiaSemana())) {
     LocalTime inicio2 = LocalTime.parse(h2.getHoraInicio(), TIME_FORMATTER);
     LocalTime fin2 = LocalTime.parse(h2.getHoraFin(), TIME_FORMATTER);

     // Verificar solapamiento
     if (haySolapamiento(inicio1, fin1, inicio2, fin2)) {
      throw new ValidationException(
        String.format("Los horarios del %s se solapan: %s-%s y %s-%s",
          h1.getDiaSemana(),
          h1.getHoraInicio(), h1.getHoraFin(),
          h2.getHoraInicio(), h2.getHoraFin()));
     }
    }
   }
  }
 }

 private boolean haySolapamiento(LocalTime inicio1, LocalTime fin1, LocalTime inicio2, LocalTime fin2) {
  return inicio1.isBefore(fin2) && inicio2.isBefore(fin1);
 }
}
