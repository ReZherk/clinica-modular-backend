package ReZherk.clinica.sistema.modules.admin.application.validator;

import org.springframework.stereotype.Component;

import ReZherk.clinica.sistema.core.domain.entity.Horario;
import ReZherk.clinica.sistema.modules.admin.application.dto.request.HorarioRequestDto;
import jakarta.validation.ValidationException;

import java.time.LocalTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class HorarioValidator {

  private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

  private static final int HORA_DESCANSO_MINUTOS = 60;
  private static final int UMBRAL_DESCANSO_MINUTOS = 6 * 60;

  public void validarHorasSemanales(List<Horario> horarios, int horasSemanalesEsperadas) {
    int totalMinutosLaborales = calcularMinutosLaboralesTotales(horarios);
    int horasLaboralesTotales = totalMinutosLaborales / 60;

    if (horasLaboralesTotales != horasSemanalesEsperadas) {
      throw new ValidationException(
          String.format("Las horas semanales no coinciden. Se esperaban %d horas pero la suma da %d horas",
              horasSemanalesEsperadas, horasLaboralesTotales));
    }
  }

  public int calcularMinutosLaboralesTotales(List<Horario> horarios) {
    return horarios.stream()
        .mapToInt(this::calcularMinutosLaborales)
        .sum();
  }

  private int calcularMinutosLaborales(Horario horario) {
    long minutosTotales = Duration.between(
        horario.getHoraInicio(),
        horario.getHoraFin()).toMinutes();

    // Si la jornada es mayor a 6 horas, tiene 1 hora de descanso
    if (minutosTotales > UMBRAL_DESCANSO_MINUTOS) {
      return (int) (minutosTotales - HORA_DESCANSO_MINUTOS);
    }

    return (int) minutosTotales;
  }

  public void validarSolapamientos(List<HorarioRequestDto> horarios) {
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
